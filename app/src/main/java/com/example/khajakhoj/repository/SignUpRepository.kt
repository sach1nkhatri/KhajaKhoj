package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon

interface SignUpRepository {

    suspend fun checkEmailExists(email: String): Boolean

    suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Result<Boolean>

    suspend fun saveUserInRealtimeDatabase(
        uid: String,
        fullName: String,
        email: String,
        phone: String,
        address: String,
        profilePicture: String,
        bookmarkedRestaurants: List<String>,
        reviews: List<String>,
        rating: Map<String, Double>,
        claimedCoupons: List<Coupon>,
        createdAt: Long,
        updatedAt: Long
    ): Result<Unit>
}
