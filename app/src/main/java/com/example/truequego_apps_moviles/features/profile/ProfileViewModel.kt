package com.example.truequego_apps_moviles.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truequego_apps_moviles.core.session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                _userName.value = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "Usuario"
                _userEmail.value = currentUser.email ?: ""
            }
        }
    }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }
}
