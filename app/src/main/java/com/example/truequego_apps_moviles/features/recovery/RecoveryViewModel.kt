package com.example.truequego_apps_moviles.features.recovery

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor() : ViewModel() {

    // Pantalla de olvido de contraseña
    var email = mutableStateOf("")

    // Pantalla de restablecimiento (código + nueva contraseña)
    val codeDigits = mutableStateListOf("", "", "", "", "")
    var newPassword = mutableStateOf("")
    var confirmNewPassword = mutableStateOf("")

    private val _recoveryResult = MutableSharedFlow<String>()
    val recoveryResult = _recoveryResult.asSharedFlow()

    fun onSendRecoveryLink() {
        viewModelScope.launch {
            when {
                email.value.isBlank() ->
                    _recoveryResult.emit("Ingresa tu correo electrónico")
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches() ->
                    _recoveryResult.emit("Correo electrónico no válido")
                else ->
                    _recoveryResult.emit("Enlace enviado a ${email.value.trim()}. Revisa tu bandeja.")
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
                else ->
                    _recoveryResult.emit("Contraseña restablecida con éxito")
            }
        }
    }
}
