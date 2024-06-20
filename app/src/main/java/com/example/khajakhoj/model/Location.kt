package com.example.khajakhoj.model

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: String = ""
)
