package com.example.khajakhoj.model

data class Coupon(
    val id: Int = 0,
    val code: String = "",
    val discount: Int = 0,
    val expirationDate: String = "",
    val restaurantId: String = ""
)
