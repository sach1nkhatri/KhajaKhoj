package com.example.khajakhoj.repository

import android.util.Log
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()

    companion object {
        const val TAG = "UserRepositoryImpl"
    }

    private lateinit var currentUserId : String

    override suspend fun checkEmailExists(email: String): Boolean {
        Log.d(TAG, "Checking if email exists: $email")
        return try {
            val query =
                databaseReference.getReference("users").orderByChild("email").equalTo(email).get()
                    .await()
            val exists = query.exists()
            Log.d(TAG, "Email exists: $exists")
            exists
        } catch (e: Exception) {
//            Log.e(TAG, "Error checking email existence", e)
            Log.d(TAG, "No email found : $email")
            false
        }
    }

    override suspend fun signUpUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        Log.d(TAG, "Signing up user with email: $email")
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userCreated = firebaseAuth.currentUser != null
            Log.d(TAG, "User signed up successfully: $userCreated")
            Result.success(userCreated)
        } catch (e: Exception) {
            Log.e(TAG, "Error signing up user", e)
            Result.failure(e)
        }
    }

    override suspend fun saveUserInRealtimeDatabase(user: User): Result<Unit> { // gets user  detail as parameter
        Log.d(TAG, "Saving user in Realtime Database : $user")
        return try {
            Log.d(TAG, "Saving uid: ${user.uid}")
            Log.d(TAG, "Saving fullName: ${user.fullName}")
            Log.d(TAG, "Saving email: ${user.email}")
            Log.d(TAG, "Saving phoneNumber: ${user.phoneNumber}")
            Log.d(TAG, "Saving address: ${user.address}")
            Log.d(TAG, "Saving profilePictureUrl: ${user.profilePictureUrl}")
            Log.d(TAG, "Saving createdAt: ${user.createdAt}")
            databaseReference.getReference("users").child(user.uid).setValue(user).await()
            Log.d(TAG, "User saved in Realtime Database successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user in Realtime Database", e)
            Result.failure(e)
        }
    }

    override suspend fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Logging in with email: $email")
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                currentUserId = currentUser.uid
                Log.d(TAG, "logged User Id : $currentUserId")
            }
            Log.d("LoginRepositoryImpl", "Login successful")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error logging in", e)
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Sending password reset email to: $email")
            firebaseAuth.sendPasswordResetEmail(email).await()
            Log.d("LoginRepositoryImpl", "Password reset email sent")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error sending password reset email", e)
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null

        val uid = firebaseUser.uid

        // Retrieve user data from Realtime Database
        val userSnapshot = try {
            val userReference = databaseReference.getReference("users").child(uid).get().await()
            userReference.value as? HashMap<String, Any?>
        } catch (e: Exception) {
            Log.e("UserRepository", "Error retrieving user data for uid: $uid", e)
            null
        }

        // Check if user data retrieved successfully
        if (userSnapshot == null) {
            Log.w("UserRepository", "User data not found for uid: $uid")
            return null
        }

        // Extract user data from snapshot
        val fullName = userSnapshot["fullName"] as? String ?: ""
        val email = firebaseUser.email ?: "" // Use email from FirebaseUser
        val phoneNumber = userSnapshot["phoneNumber"] as? String ?: ""
        val address = userSnapshot["address"] as? String ?: ""
        val profilePictureUrl = userSnapshot["profilePictureUrl"] as? String ?: ""

        // Create a custom User object with fetched data
        return User(
            uid = uid,
            email = email,
            fullName = fullName,
            phoneNumber = phoneNumber,
            address = address,
            profilePictureUrl = profilePictureUrl
            // ... other fields as needed (bookmarkedRestaurants, reviews, etc.)
        )
    }


}