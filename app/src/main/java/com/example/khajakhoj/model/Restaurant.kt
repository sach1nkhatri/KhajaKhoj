package com.example.khajakhoj.model

data class Restaurant(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val cuisineTypes: List<String> = emptyList(), // Updated property name for consistency
    val openTime: String = "",
    val closeTime: String = "",
    val contactNumber: String = "",
    val bikeParking: Boolean = false,
    val carParking: Boolean = false,
    var wifi: Boolean = false,
    val rating: Int = 0,
    val reviews: List<Review> = emptyList(),
    val menuItems: List<MenuItem> = emptyList(),
    val coupons: List<Coupon> = emptyList(),  // what is this Coupon here
    val location: Location = Location()
)