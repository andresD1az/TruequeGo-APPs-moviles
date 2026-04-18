package com.example.truequego_apps_moviles.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val location: String = "",
    val notificationRange: Int = 0
)
