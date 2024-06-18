package com.example.khajakhoj.model

data class Coupon(
    val id: Int=0,
    val code: String="",
    val discount: Int=0,
    val restaurant: String="",
    val expirationDate: String="",
    var isUsed: Boolean = false
)