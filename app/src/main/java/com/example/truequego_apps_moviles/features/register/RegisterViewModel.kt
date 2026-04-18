package com.example.truequego_apps_moviles.features.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.domain.model.User
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Paso 1
    var fullName = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")

    // Paso 2
    var location = mutableStateOf("Armenia, Quindío")
    var notificationRange = mutableStateOf("10 km")

    var currentStep = mutableStateOf(1)

    private val _registerResult = MutableSharedFlow<String>()
    val registerResult = _registerResult.asSharedFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    fun onNextStep() {
        if (fullName.value.isBlank()) {
            viewModelScope.launch { _registerResult.emit("Ingresa tu nombre completo") }
            return
        }
        if (email.value.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()) {
            viewModelScope.launch { _registerResult.emit("Ingresa un correo electrónico válido") }
            return
        }
        if (password.value.length < 8) {
            viewModelScope.launch { _registerResult.emit("La contraseña debe tener mínimo 8 caracteres") }
            return
        }
        currentStep.value = 2
    }

    fun onPreviousStep() {
        currentStep.value = 1
    }

    fun onCreateAccount() {
        _isLoading.value = true
        val rangeInt = notificationRange.value.replace(" km", "").toIntOrNull() ?: 10
        val user = User(
            fullName = fullName.value.trim(),
            email = email.value.trim(),
            location = location.value.trim(),
            notificationRange = rangeInt
        )

        viewModelScope.launch {
            authRepository.register(user, password.value).collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _registerResult.emit("REGISTER_SUCCESS")
                }.onFailure {
                    _registerResult.emit(it.message ?: "Error al crear la cuenta")
                }
            }
        }
    }
}
