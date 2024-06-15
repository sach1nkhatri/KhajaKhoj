package com.example.khajakhoj.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val profilePictureUrl: String = "",
    val bookmarkedRestaurants: List<String> = emptyList(),
    val reviews: List<String> = emptyList(),
    val rating: Map<String, Double> = emptyMap(),
    val claimedCoupons: List<Coupon> = emptyList(),
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
