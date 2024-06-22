package com.example.khajakhoj.model

data class Restaurant(
    var id: String = "",
    val name: String = "",
    val address: String = "",  // admin input address
    val cuisineType: String = "",  // Single cuisine Type
    val openTime: String = "",
    val closeTime: String = "",
    val contactNumber: String = "",
    val bikeParking: Boolean = false,
    val carParking: Boolean = false,
    var wifi: Boolean = false,
    val rating: Double = 0.0,
    val location: String = "",  // location of restaurnat for navigantion purpose
    val restaurantLogoUrl: String = ""
)




//data class Restaurant(
//    val id: String = "",
//    val name: String = "",
//    val address: String = "",
////    val cuisineTypes: List<String> = emptyList(),
//    val cuisineTypes: String = "",
//    val openTime: String = "",
//    val closeTime: String = "",
//    val contactNumber: String = "",
//    val bikeParking: Boolean = false,
//    val carParking: Boolean = false,
//    var wifi: Boolean = false,
//
//    val rating: Int = 0,
//    val reviews: Map<String, Boolean> = emptyMap(),
//    val menuItems: Map<String, Boolean> = emptyMap(),
//    val coupons: Map<String, Boolean> = emptyMap(),
//    val location: Location = Location()
//)
