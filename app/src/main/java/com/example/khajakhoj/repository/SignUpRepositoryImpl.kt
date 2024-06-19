package com.example.khajakhoj.repository

import android.util.Log
import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SignUpRepositoryImpl : SignUpRepository {

//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val databaseReference = FirebaseDatabase.getInstance()
//
//    override suspend fun checkEmailExists(email: String): Boolean {
//        Log.d("SignUpRepositoryImpl", "Checking if email exists: $email")
//        return try {
//            val query =
//                databaseReference.getReference("users").orderByChild("email").equalTo(email).get()
//                    .await()
//            val exists = query.exists()
//            Log.d("SignUpRepositoryImpl", "Email exists: $exists")
//            exists
//        } catch (e: Exception) {
//            Log.e("SignUpRepositoryImpl", "Error checking email existence", e)
//            false
//        }
//    }
//
//    override suspend fun signUpUserWithEmailAndPassword(
//        email: String,
//        password: String
//    ): Result<Boolean> {
//        Log.d("SignUpRepositoryImpl", "Signing up user with email: $email")
//        return try {
//            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            val userCreated = firebaseAuth.currentUser != null
//            Log.d("SignUpRepositoryImpl", "User signed up successfully: $userCreated")
//            Result.success(userCreated)
//        } catch (e: Exception) {
//            Log.e("SignUpRepositoryImpl", "Error signing up user", e)
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun saveUserInRealtimeDatabase(user: User): Result<Unit> {
//        Log.d("SignUpRepositoryImpl", "Saving user in Realtime Database : $user")
//        return try {
//            Log.d("SignUpRepositoryImpl", "Saving uid: ${user.uid}")
//            Log.d("SignUpRepositoryImpl", "Saving fullName: ${user.fullName}")
//            Log.d("SignUpRepositoryImpl", "Saving email: ${user.email}")
//            Log.d("SignUpRepositoryImpl", "Saving phoneNumber: ${user.phoneNumber}")
//            Log.d("SignUpRepositoryImpl", "Saving address: ${user.address}")
//            Log.d("SignUpRepositoryImpl", "Saving profilePictureUrl: ${user.profilePictureUrl}")
//            Log.d("SignUpRepositoryImpl", "Saving createdAt: ${user.createdAt}")
//            databaseReference.getReference("users").child(user.uid).setValue(user).await()
//            Log.d("SignUpRepositoryImpl", "User saved in Realtime Database successfully")
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Log.e("SignUpRepositoryImpl", "Error saving user in Realtime Database", e)
//            Result.failure(e)
//        }
//    }
}
