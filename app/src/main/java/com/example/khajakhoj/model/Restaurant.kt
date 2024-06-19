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
    val reviews: Map<String, Boolean> = emptyMap(),
    val menuItems: Map<String, Boolean> = emptyMap(),
    val coupons: Map<String, Boolean> = emptyMap(),
    val location: Location = Location()
)
