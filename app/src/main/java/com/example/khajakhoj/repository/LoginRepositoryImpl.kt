package com.example.khajakhoj.repository

import android.util.Log
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl() : LoginRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()

    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            Log.d("LoginRepositoryImpl", "Checking if email exists: $email")
            val query =
                databaseReference.getReference("users").orderByChild("email").equalTo(email).get()
                    .await()
            val exists = query.exists()
            Log.d("LoginRepositoryImpl", "Email exists: $exists")
            exists
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error checking email existence", e)
            false
        }
    }

    override suspend fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Logging in with email: $email")
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("LoginRepositoryImpl", "Login successful")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error logging in", e)
            Result.Failure(e)
        }
    }


    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Sending password reset email to: $email")
            auth.sendPasswordResetEmail(email).await()
            Log.d("LoginRepositoryImpl", "Password reset email sent")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error sending password reset email", e)
            Result.Failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null

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
