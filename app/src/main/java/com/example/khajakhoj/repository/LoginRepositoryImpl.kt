package com.example.khajakhoj.repository

import CredentialManager
import android.content.Context
import android.util.Log
import androidx.compose.ui.util.trace
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val context: Context) : LoginRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()
    private var currentUser: FirebaseUser? = null
    private val credentialManager = CredentialManager(context)

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

    override suspend fun loginUserWithEmailPassword(email: String, password: String): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Logging in with email: $email")
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            currentUser = authResult.user
            currentUser?.let {
                val user = it.toUser()
                Log.d("LoginRepositoryImpl", "Login successful")
                if (user != null) {
                    Log.d("LoginRepositoryImpl", "Saving Login State")
                    credentialManager.saveLoginState(true)
                    if(credentialManager.isLoggedIn()) {
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                        Log.d("LoginRepositoryImpl", "isLoggedIn: true")
                    }
                }
                Result.Success(true)
            } ?: run {
                Log.e("LoginRepositoryImpl", "User not found after login")
                Result.Failure(Exception("User not found"))
            }
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

    override fun getCurrentUser(): User? {
        val user = credentialManager.getSavedCredentials() ?: currentUser.toUser()
        Log.d("LoginRepositoryImpl", "Getting current user: ${user?.email}")
        return user
    }

    private fun FirebaseUser?.toUser(): User? {
        return this?.let { firebaseUser ->
            User(
                uid = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                phoneNumber = firebaseUser.phoneNumber ?: "",
                address = "",
                profilePictureUrl = firebaseUser.photoUrl?.toString() ?: "",
                bookmarkedRestaurants = emptyList(),
                reviews = emptyList(),
                rating = emptyMap(),
                claimedCoupons = emptyList(),
                createdAt = firebaseUser.metadata?.creationTimestamp ?: 0L,
                updatedAt = firebaseUser.metadata?.lastSignInTimestamp ?: 0L,
            ).also {
                Log.d("LoginRepositoryImpl", "Converted FirebaseUser to User: ${it.email}")
            }
        }
    }
}
