package com.example.khajakhoj.repository

import android.util.Log
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
            Log.d("SignUpRepositoryImpl", "Checking if email exists: $email")
            val query =
                databaseReference.getReference("users").orderByChild("email").equalTo(email).get()
                    .await()
            val exists = query.exists()
            Log.d("SignUpRepositoryImpl", "Email exists: $exists")
            exists
        } catch (e: Exception) {
            Log.e("SignUpRepositoryImpl", "Error checking email existence", e)
            false
        }
    }

    override suspend fun signUpUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            Log.d("SignUpRepositoryImpl", "Signing up user with email: $email")
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userCreated = firebaseAuth.currentUser != null
            Log.d("SignUpRepositoryImpl", "User signed up successfully: $userCreated")
            Result.success(userCreated)
        } catch (e: Exception) {
            Log.e("SignUpRepositoryImpl", "Error signing up user", e)
            Result.failure(e)
        }
    }

    override suspend fun saveUserInRealtimeDatabase(
        uid: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        address: String,
        profilePictureUrl: String,
        bookmarkedRestaurants: List<String>,
        reviews: List<String>,
        rating: Map<String, Double>,
        claimedCoupons: List<Coupon>,
        createdAt: Long,
        updatedAt: Long
    ): Result<Unit> {
        return try {
            Log.d("SignUpRepositoryImpl", "Saving user in Realtime Database with UID: $uid")
            val user = User(
                uid = uid,
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                profilePictureUrl = profilePictureUrl,
                bookmarkedRestaurants = bookmarkedRestaurants,
                reviews = reviews,
                rating = rating,
                claimedCoupons = claimedCoupons,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            databaseReference.getReference("users").child(uid).setValue(user).await()
            Log.d("SignUpRepositoryImpl", "User saved in Realtime Database successfully")
            Log.d("SignUpRepositoryImpl", "Profile Picture Url : $profilePictureUrl")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SignUpRepositoryImpl", "Error saving user in Realtime Database", e)
            Result.failure(e)
        }
    }
}
