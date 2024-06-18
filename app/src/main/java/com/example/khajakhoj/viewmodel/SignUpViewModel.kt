package com.example.khajakhoj.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.SignUpRepository
import com.example.khajakhoj.repository.SignUpRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository: SignUpRepository = SignUpRepositoryImpl()

    private val _signUpResult = MutableLiveData<Result<Boolean>>()
    val signUpResult: LiveData<Result<Boolean>> get() = _signUpResult

    fun signUpUser(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        address: String
    ) {
        Log.d("SignUpViewModel", "Starting sign-up process")
        Log.d("SignUpViewModel", "Password: $password, Confirm Password: $confirmPassword")
        if (!validateSignUpInput(fullName, email, phoneNumber, password, confirmPassword)) {
            Log.d("SignUpViewModel", "Validation failed")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("SignUpViewModel", "Checking if email exists: $email")
                val emailExists = repository.checkEmailExists(email)
                if (emailExists) {
                    Log.d("SignUpViewModel", "Email already exists")
                    _signUpResult.value = Result.failure(Exception("Email already exists"))
                } else {
                    Log.d("SignUpViewModel", "Email does not exist, proceeding with sign-up")
                    val signUpResult = repository.signUpUserWithEmailAndPassword(email, password)
                    if (signUpResult.isSuccess) {
                        Log.d("SignUpViewModel", "Sign-up successful")
                        val uid = auth.currentUser!!.uid
                        val savedUserResult = repository.saveUserInRealtimeDatabase(
                            uid,
                            fullName,
                            email,
                            phoneNumber,
                            address,
                            profilePictureUrl = "",
                            // Set default empty string for profilePicture
                            bookmarkedRestaurants = emptyList(),  // Set default empty list for bookmarked restaurants
                            claimedCoupons = emptyList(),  // Set default empty list for claimed coupons
                            createdAt = System.currentTimeMillis(),  // Set created time
                            updatedAt = System.currentTimeMillis(),  // Set updated time
                            reviews = emptyList(),  // Set default empty list for reviews
                            rating = emptyMap()  // Set default empty map for rating
                        )
                        if (savedUserResult.isSuccess) {
                            Log.d("SignUpViewModel", "User data saved successfully")
                            _signUpResult.value = Result.success(true)
                        } else {
                            Log.e("SignUpViewModel", "Failed to save user data after successful sign-up")
                            _signUpResult.value = Result.failure(Exception("Signup successful, but failed to save user data"))
                        }
                    } else {
                        Log.e("SignUpViewModel", "Sign-up failed")
                        _signUpResult.value = signUpResult // Propagate signup failure reason
                    }
                }
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Error during sign-up process", e)
                _signUpResult.value = Result.failure(e)
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
        Log.d("SignUpViewModel", "Validating sign-up input")
        return when {
            fullName.isBlank() -> {
                Log.d("SignUpViewModel", "Full name is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            email.isBlank() -> {
                Log.d("SignUpViewModel", "Email is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            phoneNumber.isBlank() -> {
                Log.d("SignUpViewModel", "Phone number is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            password.isBlank() -> {
                Log.d("SignUpViewModel", "Password is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            confirmPassword.isBlank() -> {
                Log.d("SignUpViewModel", "Confirm password is blank")
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }
            !isValidEmail(email) -> {
                Log.d("SignUpViewModel", "Invalid email address")
                _signUpResult.value = Result.failure(Exception("Invalid email address"))
                false
            }
            !validatePhoneNumber(phoneNumber) -> {
                Log.d("SignUpViewModel", "Invalid phone number")
                _signUpResult.value = Result.failure(Exception("Invalid phone number"))
                false
            }
            password.length < 6 -> {
                Log.d("SignUpViewModel", "Password is too short")
                _signUpResult.value = Result.failure(Exception("Password must be at least 6 characters"))
                false
            }
            password != confirmPassword -> {
                Log.d("SignUpViewModel", "Passwords do not match")
                _signUpResult.value = Result.failure(Exception("Passwords do not match"))
                false
            }
            else -> {
                Log.d("SignUpViewModel", "Validation successful")
                true
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val phoneNumberObj = phoneUtil.parse(phoneNumber, "NP")
            phoneUtil.isValidNumber(phoneNumberObj)
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Error validating phone number", e)
            false
        }
    }
}
