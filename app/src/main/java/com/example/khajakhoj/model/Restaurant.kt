package com.example.khajakhoj.model

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val cuisineTypes: List<String> = emptyList(),
    val openTime: String = "",
    val closeTime: String = "",
    val contactNumber: String = "",
    val bikeParking: Boolean = false,
    val carParking: Boolean = false,
    var wifi: Boolean = false,
    val rating: Int = 0,
    val reviews: List<Review> = emptyList(),
    val menuItems: List<MenuItem> = emptyList(),
    val coupons: List<Coupon> = emptyList(),
    val location: Location = Location()
)