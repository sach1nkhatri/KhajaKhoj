package com.example.khajakhoj.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.khajakhoj.model.User

interface UserRepository {
    suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Result<Boolean>
    suspend fun saveUserInRealtimeDatabase(user: User): Result<Unit>
    suspend fun loginUserWithEmailPassword(email: String, password: String): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
    suspend fun checkEmailExists(email: String): Boolean
    suspend fun getCurrentUser() : User?
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): LiveData<Result<String>>

    fun deleteUser(userId: String): LiveData<Result<Void?>>

}

