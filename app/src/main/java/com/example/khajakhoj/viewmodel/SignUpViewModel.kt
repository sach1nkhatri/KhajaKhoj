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
        Log.d("SignUpActivity", "Password: $password, Confirm Password: $confirmPassword")
        if (!validateSignUpInput(fullName, email, phoneNumber, password, confirmPassword)) {
            return
        }

        viewModelScope.launch {
            try {
                val emailExists = repository.checkEmailExists(email)
                if (emailExists) {
                    _signUpResult.value = Result.failure(Exception("Email already exists"))
                } else {
                    val signUpResult = repository.signUpUserWithEmailAndPassword(email, password)
                    if (signUpResult.isSuccess) {
                        val uid = auth.currentUser!!.uid
                        val savedUserResult = repository.saveUserInRealtimeDatabase(
                            uid,
                            fullName,
                            email,
                            phoneNumber,
                            address = "Lazimpat",
                            profilePicture = "",  // Set default empty string for profilePicture
                            bookmarkedRestaurants = emptyList(),  // Set default empty list for bookmarked restaurants
                            claimedCoupons = emptyList(),  // Set default empty list for claimed coupons
                            createdAt = System.currentTimeMillis(),  // Set created time
                            updatedAt = System.currentTimeMillis(),  // Set updated time
                            reviews = emptyList(),  // Set default empty list for reviews
                            rating = emptyMap()  // Set default empty map for rating
                        )
                        if (savedUserResult.isSuccess) {
                            _signUpResult.value = Result.success(true)
                        } else {
                            // Handle potential error saving user data after successful signup
                            _signUpResult.value = Result.failure(Exception("Signup successful, but failed to save user data"))
                        }
                    } else {
                        _signUpResult.value = signUpResult // Propagate signup failure reason
                    }
                }
            } catch (e: Exception) {
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
        return when {
            fullName.isBlank() -> {
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }

            email.isBlank() -> {
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }

            phoneNumber.isBlank() -> {
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }

            password.isBlank() -> {
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }

            confirmPassword.isBlank() -> {
                _signUpResult.value = Result.failure(Exception("Please fill in all fields"))
                false
            }

            !isValidEmail(email) -> {
                _signUpResult.value = Result.failure(Exception("Invalid email address"))
                false
            }

            !validatePhoneNumber(phoneNumber) -> {
                _signUpResult.value = Result.failure(Exception("Invalid phone number"))
                false
            }

            password.length < 6 -> {
                _signUpResult.value =
                    Result.failure(Exception("Password must be at least 6 characters"))
                false
            }

            password != confirmPassword -> {
                _signUpResult.value = Result.failure(Exception("Passwords do not match"))
                false
            }

            else -> true
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
            false
        }
    }
}