package com.example.truequego_apps_moviles.features.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    private val _loginResult = MutableSharedFlow<String>()
    val loginResult = _loginResult.asSharedFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    fun onLoginClick() {
        if (email.value.isBlank() || password.value.isBlank()) {
            viewModelScope.launch { _loginResult.emit("Por favor, completa todos los campos") }
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            authRepository.login(email.value.trim(), password.value).collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _loginResult.emit("LOGIN_SUCCESS")
                }.onFailure {
                    _loginResult.emit(it.message ?: "Error desconocido")
                }
            }
        }
    }
}
