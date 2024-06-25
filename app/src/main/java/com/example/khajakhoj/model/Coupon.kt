package com.example.khajakhoj.model

data class Coupon(
    val id:String = "",
    val code: String = "",
    val restaurantName: String = "",
    val location: String = "",
    val discountPercentage: Int = 0,
    val redeemedBy: MutableMap<String, Boolean> = mutableMapOf()
)
