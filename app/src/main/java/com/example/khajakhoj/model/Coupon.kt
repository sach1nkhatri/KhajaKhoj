package com.example.khajakhoj.model

data class Coupon(
    val id:String = "",
    val code: String = "",
    val couponKey: String = "",
    val restaurantName: String = "",
    val address: String = "", // in case a coupon is for specific outlet
    val discountPercentage: Int = 0,
    val minimumOrderPrice:Int=0,
    val validFrom: String = "",
    val validTo: String = "",
    val redeemedBy: MutableMap<String, Boolean> = mutableMapOf()
)

