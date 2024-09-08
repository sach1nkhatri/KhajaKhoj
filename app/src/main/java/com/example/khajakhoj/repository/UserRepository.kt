package com.example.khajakhoj.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil

interface UserRepository {
    suspend fun signUpUserWithEmailAndPassword(email: String, password: String): Result<Boolean>
    suspend fun saveUserInRealtimeDatabase(user: User): Result<Unit>
    fun loginUserWithEmailPassword(email: String, password: String): LiveData<Result<Boolean>>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
    suspend fun checkEmailExists(email: String): Boolean
    fun updateUserProfileImage(profileImageUri: Uri): Result<Unit>
    fun getCurrentUser(callback: (User?) -> Unit)
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        loadingUtil: LoadingUtil
    ): LiveData<Result<String>>

    fun deleteUser(userId: String): LiveData<Result<Void?>>

}
