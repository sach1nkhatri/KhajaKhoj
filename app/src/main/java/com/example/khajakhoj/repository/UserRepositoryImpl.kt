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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : UserRepository {

    companion object {
        const val TAG = "UserRepositoryImpl"
    }

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
        return try {
            databaseReference.getReference("users").child(user.uid).setValue(user).await()
            Log.d(TAG, "User saved in Realtime Database successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user in Realtime Database", e)
            Result.failure(e)
        }
    }

    override fun loginUserWithEmailPassword(email: String, password: String): LiveData<Result<Boolean>> {
        val loginResult = MutableLiveData<Result<Boolean>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginResult.value = Result.success(true)
                } else {
                    loginResult.value = Result.failure(task.exception ?: Exception("Unknown error"))
                }
            }
        return loginResult
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

    override fun getCurrentUser(callback: (User?) -> Unit) {
        val firebaseUser = firebaseAuth.currentUser ?: run {
            callback(null)
            return
        }

        val uid = firebaseUser.uid

        // Retrieve user data from Realtime Database
        val userReference = databaseReference.reference.child("users").child(uid)
        userReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userSnapshot = task.result.value as? HashMap<String, Any?>
                if (userSnapshot != null) {
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
                    Log.w("UserRepository", "User data not found for uid: $uid")
                    callback(null)
                }
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

    override fun updateUserProfileImage(profileImageUri: Uri): Result<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
            val storageRef = storage.reference.child("profileImages/$userId")

            val uploadTask = storageRef.putFile(profileImageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                // Continue with the task to get the download URL
                Log.d("User download  Url", " ${storageRef.downloadUrl}")
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result.toString()
                    Log.d("User download  Url", " $downloadUrl")
                    val userRef = databaseReference.reference.child("users").child(userId)
                    val updates = mapOf<String, Any>(
                        "profilePictureUrl" to downloadUrl
                    )
                    userRef.updateChildren(updates).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d("SignUpRepository", "Profile image updated successfully for UID: $userId")
                            Result.success(Unit)
                        } else {
                            Log.e("SignUpRepository", "Failed to update profile image in database: ${updateTask.exception?.message}", updateTask.exception)
                            Result.failure<Unit>(updateTask.exception ?: Exception("Unknown error during database update"))
                        }
                    }
                } else {
                    Log.e("SignUpRepository", "Failed to get download URL: ${task.exception?.message}", task.exception)
                    Result.failure<Unit>(task.exception ?: Exception("Unknown error during URL retrieval"))
                }
            }
            Result.success(Unit) // This might need adjustment based on async handling.
        } catch (e: Exception) {
            Log.e("SignUpRepository", "Failed to update profile image: ${e.message}", e)
            Result.failure<Unit>(e)
        }
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