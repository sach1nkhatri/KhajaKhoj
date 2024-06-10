package com.example.khajakhoj.model

data class Review(
    val id: String = "",
    val userId: String = "",
    val restaurantId: String = "",
    val rating: Double = 0.0,
    val comment: String = "",
    val createdAt: Long = 0L
)