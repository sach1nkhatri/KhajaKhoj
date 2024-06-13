package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User

//typealias EmailCheckCallback = (Boolean, String?) -> Unit

interface UserRepository {

    suspend fun checkIfEmailExists(email: String) : Boolean

    suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Boolean

    suspend fun saveUserInRealtimeDatabase(
        uid: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        address: String,
        bookmarkedRestaurants: List<String> = emptyList(),
        reviews: List<String> = emptyList(),
        rating: Map<String, Double> = emptyMap(),
        claimedCoupons: List<Coupon> = emptyList(),
        createdAt: Long = System.currentTimeMillis(),
        updatedAt: Long = System.currentTimeMillis()
    ): Result<Unit>

    suspend fun signInUserWithEmailAndPassword(email: String, password: String): Result<Unit>
//     signs in the user with the given email and password

    suspend fun sendPasswordResetEmail(email: String) : Result<Unit>
    // sends a password reset email to the user with the given email

//    suspend fun getUserProfile(uid: String): Result<User>
    // gets the user profile with the given uid

//    suspend fun signOutUser(): Result<Unit>
    // signs out the user

//    suspend fun updateUserProfile()
//        uid: String,
//        fullName: String,
//        email: String,
//        address: String,
//        profilePictureUrl: String,
//        phoneNumber: String
//    through this method i want to be able to update either of the user attributes


}

