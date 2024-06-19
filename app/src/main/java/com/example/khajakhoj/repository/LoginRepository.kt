package com.example.khajakhoj.repository

import androidx.lifecycle.LiveData
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Result

interface LoginRepository {
    suspend fun checkEmailExists(email: String): Boolean
    suspend fun loginUserWithEmailPassword(email: String, password: String): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
    suspend fun getCurrentUser() : User?
}
