package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.repository.UserRepository
import com.example.khajakhoj.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.events.Event
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(private val repository: UserRepository = UserRepositoryImpl()) : ViewModel() {

    private val _userResponse = MutableLiveData<Result<Unit>>()
    val userResponse: LiveData<Result<Unit>> = _userResponse

    fun signUpUser(
        fullName: String,
        email: String,
        phoneNumber: String,
        address: String,
        password: String,
        confirmPassword: String
    ) {
        if (!validateSignUpInput(fullName, email, phoneNumber, password, confirmPassword)) return

        viewModelScope.launch {
            try {
                // Check if email exists
                val emailExists = repository.checkIfEmailExists(email)
                if (emailExists) {
                    _userResponse.value = Result.failure(Exception("Email already exists"))
                } else {
                    // Sign up user with email and password
                    val signUpSuccessful =
                        repository.signUpUserWithEmailAndPassword(email, password)
                    if (signUpSuccessful) {
                        // Sign up successful, save user data
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                            ?: throw Exception("Failed to get user UID")
                        val saveResult = repository.saveUserInRealtimeDatabase(
                            uid = uid,
                            fullName = fullName,
                            email = email,
                            phoneNumber = phoneNumber,
                            address = address
                        )
                        _userResponse.value = saveResult
                    } else {
                        _userResponse.value = Result.failure(Exception("Sign up failed"))
                    }
                }
            } catch (e: Exception) {
                _userResponse.value = Result.failure(e)
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
        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _userResponse.value = Result.failure(Exception("Please fill in all fields"))
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _userResponse.value = Result.failure(Exception("Invalid email address"))
            return false
        }

        if (!validatePhoneNumber(phoneNumber)) {
            _userResponse.value = Result.failure(Exception("Invalid phone number"))
            return false
        }

        if (password.length < 6) {
            _userResponse.value =
                Result.failure(Exception("Password must be at least 6 characters"))
            return false
        }

        if (password != confirmPassword) {
            _userResponse.value = Result.failure(Exception("Passwords do not match"))
            return false
        }
        return true
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

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                val signInResult = repository.signInUserWithEmailAndPassword(email, password)
                _userResponse.value = signInResult
            } catch (e: FirebaseAuthInvalidUserException) {
                _userResponse.value = Result.failure(Exception("No account with this email"))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _userResponse.value = Result.failure(Exception("Invalid password"))
            } catch (e: Exception) {
                _userResponse.value = Result.failure(e)
            }
        }
    }

    private val _passwordResetResult = MutableLiveData<Result<Unit>>()
    val passwordResetResult: LiveData<Result<Unit>> = _passwordResetResult

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _passwordResetResult.value = repository.sendPasswordResetEmail(email)
        }
    }
}
