package com.example.khajakhoj.repository

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UserRepositoryImpl : UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()


    companion object {
        const val TAG = "UserRepositoryImpl"
    }

    private lateinit var currentUserId: String

    override fun checkEmailExists(email: String): Boolean {
        Log.d(TAG, "Checking if email exists: $email")
        return try {
            val query =
                databaseReference.getReference("users").orderByChild("email").equalTo(email).get()
            val exists = query.result?.exists() ?: false
            Log.d(TAG, "Email exists: $exists")
            exists
        } catch (e: DatabaseException) {
            Log.e(TAG, "Error checking email existence: ${e.message}")
            false
        }
    }

    override fun signUpUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        Log.d(TAG, "Signing up user with email: $email")
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
            val userCreated = firebaseAuth.currentUser != null
            Log.d(TAG, "User signed up successfully: $userCreated")
            Result.success(userCreated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveUserInRealtimeDatabase(user: User): Result<Unit> { // gets user  detail as parameter
        Log.d(TAG, "Saving user in Realtime Database : $user")
        return try {
            Log.d(TAG, "Saving uid: ${user.uid}")
            Log.d(TAG, "Saving fullName: ${user.fullName}")
            Log.d(TAG, "Saving email: ${user.email}")
            Log.d(TAG, "Saving phoneNumber: ${user.phoneNumber}")
            Log.d(TAG, "Saving profilePictureUrl: ${user.profilePictureUrl}")
            Log.d(TAG, "Saving createdAt: ${user.createdAt}")
            databaseReference.getReference("users").child(user.uid).setValue(user)
            Log.d(TAG, "User saved in Realtime Database successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user in Realtime Database", e)
            Result.failure(e)
        }
    }

    override fun updateUserProfileImage(profileImageUri: Uri): Result<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            val storageRef = storage.reference.child("profileImages/$userId")

            storageRef.putFile(profileImageUri)

            val downloadUrl = storageRef.downloadUrl.toString()

            val userRef = databaseReference.reference.child("users").child(userId)
            val updates = mapOf<String, Any>(
                "profilePictureUrl" to downloadUrl
            )
            userRef.updateChildren(updates)

            Log.d("SignUpRepository", "Profile image updated successfully for UID: $userId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SignUpRepository", "Failed to update profile image: ${e.message}", e)
            Result.failure(e)
        }
    }


    override fun loginUserWithEmailPassword(
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Logging in with email: $email")
            firebaseAuth.signInWithEmailAndPassword(email, password)
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

    override fun sendPasswordResetEmail(email: String): Result<Boolean> {
        return try {
            Log.d("LoginRepositoryImpl", "Sending password reset email to: $email")
            firebaseAuth.sendPasswordResetEmail(email)
            Log.d("LoginRepositoryImpl", "Password reset email sent")
            Result.success(true)
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error sending password reset email", e)
            Result.failure(e)
        }
    }

    override fun getCurrentUser(callback: (User?) -> Unit) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            callback(null)
            return
        }

        val uid = firebaseUser.uid
        val userReference = databaseReference.reference.child("users").child(uid)

        userReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val userSnapshot = dataSnapshot.value as? HashMap<String, Any?>

                if (userSnapshot == null) {
                    Log.w("UserRepository", "User data not found for uid: $uid")
                    callback(null)
                    return@addOnCompleteListener
                }

                // Extract user data from snapshot
                val fullName = userSnapshot["fullName"] as? String ?: ""
                val email = firebaseUser.email ?: "" // Use email from FirebaseUser
                val phoneNumber = userSnapshot["phoneNumber"] as? String ?: ""
                val profilePictureUrl = userSnapshot["profilePictureUrl"] as? String ?: ""
                val createdAt = userSnapshot["createdAt"] as? Long ?: 0

                // Create a custom User object with fetched data
                val user = User(
                    uid = uid,
                    email = email,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    profilePictureUrl = profilePictureUrl,
                    createdAt = createdAt
                )
                callback(user)
            } else {
                Log.e("UserRepository", "Error retrieving user data for uid: $uid", task.exception)
                callback(null)
            }
        }
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

    @SuppressLint("SuspiciousIndentation")
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