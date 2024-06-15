package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl : UserRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override suspend fun checkIfEmailExists(email: String): Boolean {
        val database = FirebaseDatabase.getInstance().reference
        return try {
            val query = database.child("users").orderByChild("email").equalTo(email).get().await()
            query.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Boolean {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult.user != null
        } catch (e: Exception) {
            false
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
            val user = User(
                uid = uid,
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                bookmarkedRestaurants = bookmarkedRestaurants,
                reviews = reviews,
                rating = rating,
                claimedCoupons = claimedCoupons,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            database.child("users").child(uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInUserWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> Result.failure(Error("Email address not found or invalid"))
                else -> Result.failure(e)
            }
        }
    }
}