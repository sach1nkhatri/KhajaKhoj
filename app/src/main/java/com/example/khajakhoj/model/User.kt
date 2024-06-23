package com.example.khajakhoj.model

data class User(
    val uid: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val profilePictureUrl: String,
    val bookmarkedRestaurants: Map<String, Boolean> = emptyMap(),
    val reviews: Map<String, Boolean> = emptyMap(),
    val ratings: Map<String, Double> = emptyMap(),
    val claimedCoupons: Map<String, Boolean> = emptyMap(),
    val createdAt: Long
)
