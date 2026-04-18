package com.example.truequego_apps_moviles.domain.model

data class Product(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val condition: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
