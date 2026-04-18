package com.example.truequego_apps_moviles.domain.model

data class TradeProposal(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderAvatarUrl: String = "",
    val receiverId: String = "",
    val offeredProductId: String = "",
    val offeredProductTitle: String = "",
    val offeredProductImage: String = "",
    val requestedProductId: String = "",
    val requestedProductTitle: String = "",
    val requestedProductImage: String = "",
    val message: String = "",
    val status: String = "PENDING", // PENDING, ACCEPTED, REJECTED
    val createdAt: Long = System.currentTimeMillis()
)
