package com.example.truequego_apps_moviles.features.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

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

    fun onNextStep() {
        viewModelScope.launch {
            when {
                fullName.value.isBlank() ->
                    _registerResult.emit("Ingresa tu nombre completo")
                email.value.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.trim()).matches() ->
                    _registerResult.emit("Ingresa un correo electrónico válido")
                password.value.length < 8 ->
                    _registerResult.emit("La contraseña debe tener mínimo 8 caracteres")
                else ->
                    currentStep.value = 2
            }
        }
    }

    fun onPreviousStep() {
        currentStep.value = 1
    }

    fun onCreateAccount() {
        viewModelScope.launch {
            _registerResult.emit("Cuenta creada exitosamente")
        }
    }
}
