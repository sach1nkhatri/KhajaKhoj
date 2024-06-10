package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SignupRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val query =
                database.getReference("users").orderByChild("email").equalTo(email).get().await()
            query.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signUpUser(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResult.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserData(
        uid: String,
        fullName: String,
        email: String,
        phone: String,
        bookmarkedRestaurants: List<String> = emptyList(),
        reviews: List<String> = emptyList(),
        rating: Map<String, Double> = emptyMap(),
        claimedCoupons: List<Coupon> = emptyList(),
        createdAt: Long = System.currentTimeMillis(),
        updatedAt: Long = System.currentTimeMillis()
    ): Result<Unit> {
        return try {
            val user = User(
                uid = uid,
                fullName = fullName,
                email = email,
                phoneNumber = phone,
                profilePictureUrl = "",
                bookmarkedRestaurants = bookmarkedRestaurants,
                reviews = reviews,
                rating = rating,
                claimedCoupons = claimedCoupons,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            database.getReference("users").child(uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfilePicture(uid: String, profilePictureUrl: String): Result<Unit> {
        return try {
            database.getReference("users").child(uid).child("profilePictureUrl")
                .setValue(profilePictureUrl).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}