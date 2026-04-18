package com.example.truequego_apps_moviles.data.repository

import com.example.truequego_apps_moviles.core.session.SessionManager
import com.example.truequego_apps_moviles.domain.model.User
import com.example.truequego_apps_moviles.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : AuthRepository {

    override val currentUser: User?
        get() = auth.currentUser?.let {
            User(uid = it.uid, email = it.email ?: "")
        }

    override fun login(email: String, password: String): Flow<Result<Unit>> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                CoroutineScope(Dispatchers.IO).launch {
                    sessionManager.saveSession(
                        uid = user?.uid ?: "",
                        email = user?.email ?: "",
                        name = user?.displayName ?: ""
                    )
                }
                trySend(Result.success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }

    override fun register(user: User, password: String): Flow<Result<Unit>> = callbackFlow {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: ""
                val userWithId = user.copy(uid = uid)
                firestore.collection("users").document(uid).set(userWithId)
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            sessionManager.saveSession(
                                uid = uid,
                                email = user.email,
                                name = user.fullName
                            )
                        }
                        trySend(Result.success(Unit))
                    }
                    .addOnFailureListener {
                        trySend(Result.failure(it))
                    }
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }

    override fun logout() {
        auth.signOut()
    }

    override fun sendPasswordResetEmail(email: String): Flow<Result<Unit>> = callbackFlow {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                trySend(Result.success(Unit))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }
}
