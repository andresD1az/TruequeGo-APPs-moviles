package com.example.truequego_apps_moviles.domain.repository

import com.example.truequego_apps_moviles.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: User?
    fun login(email: String, password: String): Flow<Result<Unit>>
    fun register(user: User, password: String): Flow<Result<Unit>>
    fun logout()
    fun sendPasswordResetEmail(email: String): Flow<Result<Unit>>
}
