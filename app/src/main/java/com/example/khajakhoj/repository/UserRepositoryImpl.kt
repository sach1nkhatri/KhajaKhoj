package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun checkIfEmailExists(email: String): Boolean {
        return try {
            val query = database.getReference("users").orderByChild("email").equalTo(email).get().await()
            query.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true // Signup successful
        } catch (e: Exception) {
            false // Signup failed
        }
    }

    override suspend fun saveUserInRealtimeDatabase(
        uid: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        address: String,
        bookmarkedRestaurants: List<String>,
        reviews: List<String>,
        rating: Map<String, Double>,
        claimedCoupons: List<Coupon>,
        createdAt: Long,
        updatedAt: Long
    ): Result<Unit> {
        return try {
            val user = hashMapOf(
                "uid" to uid,
                "fullName" to fullName,
                "email" to email,
                "phoneNumber" to phoneNumber,
                "address" to address,
                "bookmarkedRestaurants" to bookmarkedRestaurants,
                "reviews" to reviews,
                "rating" to rating,
                "claimedCoupons" to claimedCoupons,
                "createdAt" to createdAt,
                "updatedAt" to updatedAt
            )

            database.getReference("users").child(uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInUserWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
//
//    override suspend fun updateProfilePicture(uid: String, profilePictureUrl: String): Result<Unit> {
//        return try {
//            database.getReference("users").child(uid).child("profilePictureUrl")
//                .setValue(profilePictureUrl).await()
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}