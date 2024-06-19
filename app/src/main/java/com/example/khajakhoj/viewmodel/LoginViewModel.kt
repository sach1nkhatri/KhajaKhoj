package com.example.khajakhoj.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.LoginRepository
import com.example.khajakhoj.repository.LoginRepositoryImpl
import com.example.khajakhoj.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository: LoginRepository = LoginRepositoryImpl()

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> = _loginResult

    private val _resetPasswordResult = MutableLiveData<Result<Boolean>>()
    val resetPasswordResult: LiveData<Result<Boolean>> = _resetPasswordResult

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun signInUser(email: String, password: String) {
        Log.d("LoginViewModel", "Attempting to sign in user with email: $email")
        viewModelScope.launch {
            val result = repository.loginUserWithEmailPassword(email, password)
            if (result is Result.Success && result.data) {
//                val user = repository.getCurrentUser()
//                Log.d("LoginViewModel", "User signed in successfully: ${user?.uid}")
//                Log.d("LoginViewModel", "User signed in successfully: ${user?.fullName}")
//                Log.d("LoginViewModel", "User signed in successfully: ${user?.email}")
//                Log.d("LoginViewModel", "User signed in successfully: ${user?.phoneNumber}")
//                Log.d("LoginViewModel", "User signed in successfully: ${user?.address}")
//                _currentUser.postValue(user)
            } else {
                Log.e("LoginViewModel", "Sign-in failed for email: $email")
            }
            _loginResult.postValue(result)
        }
    }

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
}
