package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.UserRepositoryImpl
//import com.example.khajakhoj.utils.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
//    private val credentialManager: CredentialManager = CredentialManager(context = LoginPage())
    private val repository: UserRepositoryImpl = UserRepositoryImpl()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

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
        when {
            fullName.isBlank() -> return setError("Please fill in all fields")
            email.isBlank() -> return setError("Please fill in all fields")
            phoneNumber.isBlank() -> return setError("Please fill in all fields")
            password.isBlank() -> return setError("Please fill in all fields")
            confirmPassword.isBlank() -> return setError("Please fill in all fields")
            !isValidEmail(email) -> return setError("Invalid email address")
            !validatePhoneNumber(phoneNumber) -> return setError("Invalid phone number")
            password.length < 6 -> return setError("Password must be at least 6 characters")
            password != confirmPassword -> return setError("Passwords do not match")
            else -> return true
        }
    }

    private fun setError(message: String): Boolean {
        _userResponse.value = Result.failure(Exception(message))
        return false
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

    fun signIn(email: String, password: String) {
//        enablePersistence()  // Enable persistence before sign-in
        viewModelScope.launch {
            try {
                val signInResult = repository.signInUserWithEmailAndPassword(email, password)
                if (signInResult.isSuccess) {
                    saveUserCredentials(firebaseAuth.currentUser)
                    _userResponse.value = Result.success(Unit)
                }
                _userResponse.value = signInResult
            } catch (e: FirebaseAuthInvalidUserException) {
                _userResponse.value = Result.failure(NoAccountException())
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _userResponse.value = Result.failure(InvalidPasswordException())
            } catch (e: Exception) {
                _userResponse.value = Result.failure(UnknownErrorException())
            }
        }
    }

    private fun saveUserCredentials(currentUser: FirebaseUser?) {
        currentUser?.let {
            val user = User(
                fullName = it.displayName ?: "",
                email = it.email ?: "",
                address = "" // Add logic to retrieve address if needed
            )
//            credentialManager.saveSharedCredentials(user)
        }
    }

    class NoAccountException : Throwable("No account with this email")
    class InvalidPasswordException : Throwable("Invalid password")
    class UnknownErrorException : Throwable("Unknown error")


    private fun enablePersistence() {
        firebaseDatabase.setPersistenceEnabled(true)
    }

    private val _passwordResetResult = MutableLiveData<Result<Unit>>()
    val passwordResetResult: LiveData<Result<Unit>> = _passwordResetResult

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val emailExists = repository.checkIfEmailExists(email)
            if (emailExists) {
                _passwordResetResult.value = repository.sendPasswordResetEmail(email)
            } else {
                _passwordResetResult.value =
                    Result.failure(Error("Email address not found or invalid"))
            }
        }
    }
}
