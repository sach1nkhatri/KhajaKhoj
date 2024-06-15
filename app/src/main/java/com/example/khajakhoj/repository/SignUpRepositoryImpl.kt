package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SignUpRepositoryImpl : SignUpRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()

    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val query =
                databaseReference.getReference("users").orderByChild("email").equalTo(email).get().await()
            query.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUpUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(firebaseAuth.currentUser != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUserInRealtimeDatabase(
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
    ): Result<Unit> {
        return try {
            val user = User(
                uid = uid,
                fullName = fullName,
                email = email,
                phoneNumber = phone,
                address = address,
                profilePictureUrl = "",
                bookmarkedRestaurants = bookmarkedRestaurants,
                reviews = reviews,
                rating = rating,
                claimedCoupons = claimedCoupons,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            databaseReference.getReference("users").child(uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    }
