package com.example.khajakhoj.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()

    companion object {
        const val TAG = "UserRepositoryImpl"
    }

    private lateinit var currentUserId: String

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

    override suspend fun getCurrentUser(): User? {
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
        val profilePictureUrl = userSnapshot["profilePictureUrl"] as? String ?: ""
        val createdAt = userSnapshot["createdAt"] as? Long ?: 0

        // Create a custom User object with fetched data
        return User(
            uid = uid,
            email = email,
            fullName = fullName,
            phoneNumber = phoneNumber,
            profilePictureUrl = profilePictureUrl,
            createdAt = createdAt
        )
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        loadingUtil: LoadingUtil
    ): LiveData<Result<String>> {
        val resultLiveData = MutableLiveData<Result<String>>()

        if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()) {
            if (currentPassword != newPassword) {
                if (newPassword == confirmNewPassword) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.email != null) {
                        loadingUtil.showLoading()
                        val credential =
                            EmailAuthProvider.getCredential(user.email!!, currentPassword)
                        user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { passwordChange ->
                                        if (passwordChange.isSuccessful) {
                                            resultLiveData.postValue(Result.success("Password Changed Successfully"))
                                        } else {
                                            resultLiveData.postValue(Result.failure(Exception("Failed to change password")))
                                        }
                                    }
                            } else {
                                loadingUtil.dismiss()
                                resultLiveData.postValue(Result.failure(Exception("Current password is wrong.")))
                            }
                        }
                    } else {
                        resultLiveData.postValue(Result.failure(Exception("User authentication failed")))
                    }
                } else {
                    resultLiveData.postValue(Result.failure(Exception("New password and confirm password do not match.")))
                }
            } else {
                resultLiveData.postValue(Result.failure(Exception("New password and current password cannot be similar.")))
            }
        } else {
            resultLiveData.postValue(Result.failure(Exception("Please fill all the fields.")))
        }

        return resultLiveData
    }

    override fun deleteUser(userId: String): LiveData<Result<Void?>> {
        val result = MutableLiveData<Result<Void?>>()
        databaseReference.getReference("users").child(userId).removeValue()
            .addOnSuccessListener {
                result.value = Result.success(null)
            }
            .addOnFailureListener {
                result.value = Result.failure(it)
            }
        return result
    }


}