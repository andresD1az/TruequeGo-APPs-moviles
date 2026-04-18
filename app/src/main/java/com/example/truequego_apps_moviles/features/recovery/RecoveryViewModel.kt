package com.example.truequego_apps_moviles.features.recovery

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Pantalla de olvido de contraseña
    var email = mutableStateOf("")

    // Pantalla de restablecimiento (código + nueva contraseña)
    val codeDigits = mutableStateListOf("", "", "", "", "")
    var newPassword = mutableStateOf("")
    var confirmNewPassword = mutableStateOf("")

    private val _recoveryResult = MutableSharedFlow<String>()
    val recoveryResult = _recoveryResult.asSharedFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    fun onSendRecoveryLink() {
        if (email.value.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches()) {
            viewModelScope.launch { _recoveryResult.emit("Ingresa un correo electrónico válido") }
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            authRepository.sendPasswordResetEmail(email.value.trim()).collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _recoveryResult.emit("RECOVERY_EMAIL_SENT")
                }.onFailure {
                    _recoveryResult.emit(it.message ?: "Error al enviar el enlace")
                }
            }
        }
    }

    fun onResetPassword() {
        viewModelScope.launch {
            val code = codeDigits.joinToString("")
            when {
                code.length != 5 ->
                    _recoveryResult.emit("Ingresa el código de 5 dígitos completo")
                newPassword.value.length < 8 ->
                    _recoveryResult.emit("La contraseña debe tener mínimo 8 caracteres")
                newPassword.value != confirmNewPassword.value ->
                    _recoveryResult.emit("Las contraseñas no coinciden")
                else -> {
                    // Firebase maneja el restablecimiento mediante el enlace enviado al correo.
                    // Esta pantalla de código manual es demostrativa en esta etapa si no se usa un backend propio.
                    _recoveryResult.emit("Funcionalidad de código pendiente de integración con backend")
                }
            }
        }
    }
}
