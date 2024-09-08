package com.example.khajakhoj.model

data class Review(
    var reviewId: String = "",
    val restaurantId: String = "",
    var userId: String = "",
    var username: String = "",
    val rating: Double = 0.0,
    val reviewText: String = "",
    val timestamp: Long = 0L
)
