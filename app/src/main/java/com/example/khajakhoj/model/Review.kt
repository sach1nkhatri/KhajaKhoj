package com.example.khajakhoj.model

data class Review(
    val id: String = "", // reviewId
    val userId: String = "", // id of user s who wrote the review
    val restaurantId: String = "", // id of restaurant
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Long = 0L
)