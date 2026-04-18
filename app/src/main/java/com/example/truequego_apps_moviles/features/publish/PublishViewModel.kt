package com.example.truequego_apps_moviles.features.publish

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.domain.model.Product
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import com.example.truequego_apps_moviles.domain.repository.ProductRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var condition = mutableStateOf("")
    var imageUrl = mutableStateOf("")
    var locationName = mutableStateOf("")

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    private val _result = MutableSharedFlow<String>()
    val result = _result.asSharedFlow()

    @SuppressLint("MissingPermission")
    fun publish() {
        if (title.value.isBlank()) {
            viewModelScope.launch { _result.emit("El título es obligatorio") }
            return
        }
        if (description.value.isBlank()) {
            viewModelScope.launch { _result.emit("La descripción es obligatoria") }
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val (lat, lon) = getBestLocation()

            val product = Product(
                ownerId = authRepository.currentUser?.uid ?: "",
                title = title.value.trim(),
                description = description.value.trim(),
                condition = condition.value.trim().ifBlank { "Usado" },
                imageUrl = imageUrl.value.trim(),
                location = locationName.value.trim(),
                latitude = lat,
                longitude = lon
            )

            productRepository.addProduct(product).collectLatest { result ->
                _isLoading.value = false
                result.onSuccess { _result.emit("OK") }
                    .onFailure { _result.emit(it.message ?: "Error al publicar") }
            }
        }
    }

    /**
     * Intenta obtener la última ubicación conocida.
     * Si es null (GPS nunca activado), solicita una ubicación fresca con timeout de 5s.
     * Si falla todo, devuelve (0.0, 0.0).
     */
    @SuppressLint("MissingPermission")
    private suspend fun getBestLocation(): Pair<Double, Double> {
        return try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)

            // 1. Intentar última ubicación conocida
            val last = fusedClient.lastLocation.await()
            if (last != null) {
                return Pair(last.latitude, last.longitude)
            }

            // 2. Si es null, pedir ubicación fresca (una sola vez)
            suspendCancellableCoroutine { cont ->
                val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                    .setMaxUpdates(1)
                    .build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        fusedClient.removeLocationUpdates(this)
                        val loc = result.lastLocation
                        if (loc != null) cont.resume(Pair(loc.latitude, loc.longitude))
                        else cont.resume(Pair(0.0, 0.0))
                    }
                }

                fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

                cont.invokeOnCancellation {
                    fusedClient.removeLocationUpdates(callback)
                }
            }
        } catch (e: Exception) {
            Pair(0.0, 0.0)
        }
    }
}
