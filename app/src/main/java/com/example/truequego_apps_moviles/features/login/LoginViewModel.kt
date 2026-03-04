package com.example.truequego_apps_moviles.features.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    private val _loginResult = MutableSharedFlow<String>()
    val loginResult = _loginResult.asSharedFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            when {
                email.value.isBlank() || password.value.isBlank() ->
                    _loginResult.emit("Por favor, completa todos los campos")
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches() ->
                    _loginResult.emit("Ingresa un correo electrónico válido")
                password.value.length < 6 ->
                    _loginResult.emit("La contraseña debe tener al menos 6 caracteres")
                else ->
                    _loginResult.emit("Inicio de sesión exitoso")
            }
        }
    }
}
