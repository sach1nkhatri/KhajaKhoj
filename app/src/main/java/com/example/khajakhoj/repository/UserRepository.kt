package com.example.khajakhoj.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil

interface UserRepository {
    fun signUpUserWithEmailAndPassword(email: String, password: String): Result<Boolean>
    fun saveUserInRealtimeDatabase(user: User): Result<Unit>
    fun updateUserProfileImage(profileImageUri: Uri): Result<Unit>
    fun loginUserWithEmailPassword(email: String, password: String): Result<Boolean>
    fun sendPasswordResetEmail(email: String): Result<Boolean>
    fun checkEmailExists(email: String): Boolean
    fun getCurrentUser(callback: (User?) -> Unit)
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        loadingUtil: LoadingUtil
    ): LiveData<Result<String>>

    fun deleteUser(userId: String): LiveData<Result<Void?>>

}

