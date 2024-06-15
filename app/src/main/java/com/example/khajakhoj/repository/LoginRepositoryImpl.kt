package com.example.khajakhoj.repository

import CredentialManager
import android.content.Context
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val context: Context) : LoginRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null
    private val credentialManager = CredentialManager(context)

    override suspend fun loginUserWithEmailPassword(email: String, password: String): Result<Boolean> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            currentUser = authResult.user
            currentUser?.let {
                val user = it.toUser()
                if (user != null) {
                    credentialManager.saveUserCredentials(user)
                    credentialManager.getSavedCredentials()
                }
                Result.Success(true)
            } ?: Result.Failure(Exception("User not found"))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        return credentialManager.getSavedCredentials() ?: currentUser.toUser()
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
            )
        }
    }
}
