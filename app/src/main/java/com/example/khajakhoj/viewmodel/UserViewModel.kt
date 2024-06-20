package com.example.khajakhoj.viewmodel

//import com.example.khajakhoj.utils.CredentialManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.UserRepository
import com.example.khajakhoj.repository.UserRepositoryImpl
import com.example.khajakhoj.utils.ValidationUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val repository: UserRepository = UserRepositoryImpl()

    private val _signUpResult = MutableLiveData<Result<Boolean>>()
    val signUpResult: LiveData<Result<Boolean>> get() = _signUpResult

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> get() = _loginResult

    private val _resetPasswordResult = MutableLiveData<Result<Boolean>>()
    val resetPasswordResult: LiveData<Result<Boolean>> = _resetPasswordResult

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    companion object{
        private val TAG = "UserViewModel"
    }

    fun signUpUser(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        address: String
    ) {
        Log.d(TAG, "Starting sign-up process")
        Log.d(TAG, "Password: $password, Confirm Password: $confirmPassword")
        if (!validateSignUpInput(fullName, email, phoneNumber, password, confirmPassword)) {
            Log.d(TAG, "Validation failed")
            return
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Checking if email exists: $email")
                val emailExists = repository.checkEmailExists(email)
                if (emailExists) {
                    Log.d(TAG, "Email already exists")
                    _signUpResult.value = Result.failure(Exception("Email already exists"))
                } else {
                    Log.d(TAG, "Email does not exist, proceeding with sign-up")
                    val signUpResult = repository.signUpUserWithEmailAndPassword(email, password)
                    if (signUpResult.isSuccess) {
                        Log.d(TAG, "Sign-up successful")
                        val uid = auth.currentUser!!.uid
                        val user = User(
                            uid = uid,
                            fullName = fullName,
                            email = email,
                            phoneNumber = phoneNumber,
                            address = address,
                            profilePictureUrl = "",
                            bookmarkedRestaurants = emptyMap(),
                            reviews = emptyMap(),
                            ratings = emptyMap(),
                            claimedCoupons = emptyMap(),
                            createdAt = System.currentTimeMillis()
                        )
                        val savedUserResult = repository.saveUserInRealtimeDatabase(user)
                        if (savedUserResult.isSuccess) {
                            Log.d(TAG, "User data saved successfully")
                            _signUpResult.value = Result.success(true)
                        } else {
                            Log.e(TAG, "Failed to save user data after successful sign-up")
                            _signUpResult.value = Result.failure(Exception("Signup successful, but failed to save user data"))
                        }
                    } else {
                        Log.e(TAG, "Sign-up failed")
                        _signUpResult.value = signUpResult // Propagate signup failure reason
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during sign-up process", e)
                _signUpResult.value = Result.failure(e)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        Log.d(TAG, "Attempting to sign in user with email: $email")
        viewModelScope.launch {
            val result = repository.loginUserWithEmailPassword(email, password)
            if (result.isSuccess) {
                Log.d(TAG, "Sign-in successful")
            } else {
                Log.e("LoginViewModel", "Sign-in failed for email: $email")
            }
            _loginResult.postValue(result)
        }
    }

    // for displaying data in ProfileActivity
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    init {
        viewModelScope.launch {
            val user = repository.getCurrentUser()
            _currentUser.value = user
        }
    }
    // for displaying data in ProfileActivity


    fun sendPasswordResetEmail(email: String) {
        Log.d("LoginViewModel", "Attempting to send password reset email to: $email")
        viewModelScope.launch {
            val emailExists = repository.checkEmailExists(email)
            if (emailExists) {
                try {
                    val result = repository.sendPasswordResetEmail(email)
                    _resetPasswordResult.postValue(result)
                    // Success: Show toast using view model communication
                    _toastMessage.postValue("Password reset instructions sent!")
                    Log.d("LoginViewModel", "Password reset email sent to: $email")
                } catch (e: Exception) {
                    // Handle any exceptions during password reset
                    _toastMessage.postValue("Error sending password reset email: ${e.message}")
                    Log.e("LoginViewModel", "Error sending password reset email to: $email", e)
                }
            } else {
                // Email not found: Show toast using view model communication
                _toastMessage.postValue("Email not found. Please check the address.")
                Log.d("LoginViewModel", "Email not found: $email")
            }
        }
    }


    private fun validateSignUpInput(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        Log.d(TAG, "Validating sign-up input")
        return when {
            fullName.isBlank() -> {
                Log.d(TAG, "Full name is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            email.isBlank() -> {
                Log.d(TAG, "Email is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            phoneNumber.isBlank() -> {
                Log.d(TAG, "Phone number is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            password.isBlank() -> {
                Log.d(TAG, "Password is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            confirmPassword.isBlank() -> {
                Log.d(TAG, "Confirm password is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            !ValidationUtils.isValidEmail(email) -> {
                Log.d(TAG, "Invalid email address")
                _signUpResult.value = Result.failure(Exception("Invalid email address"))
                false
            }
            !ValidationUtils.validatePhoneNumber(phoneNumber) -> {
                Log.d(TAG, "Invalid phone number")
                _signUpResult.value = Result.failure(Exception("Invalid phone number"))
                false
            }
            password.length < 6 -> {
                Log.d(TAG, "Password is too short")
                _signUpResult.value = Result.failure(Exception("Password must be at least 6 characters"))
                false
            }
            password != confirmPassword -> {
                Log.d(TAG, "Passwords do not match")
                _signUpResult.value = Result.failure(Exception("Passwords do not match"))
                false
            }
            else -> {
                Log.d(TAG, "Validation successful")
                true
            }
        }
    }
}
