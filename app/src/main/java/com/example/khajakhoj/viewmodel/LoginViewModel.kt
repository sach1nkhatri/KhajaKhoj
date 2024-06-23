package com.example.khajakhoj.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()
    private val _loginResponse = MutableLiveData<Result<Unit>>()
    val loginResponse: LiveData<Result<Unit>> get() = _loginResponse

    private val _userData = MutableLiveData<Result<User?>>()
    val userData: LiveData<Result<User?>> get() = _userData

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val loginResponse = loginRepository.loginUser(username, password)
                _loginResponse.value = loginResponse
            } catch (e: Exception) {
                _loginResponse.value = Result.failure(e)
            }
        }
    }

    fun fetchUserData(uid: String) {
        viewModelScope.launch {
            try {
                val userResponse = loginRepository.fetchUserData(uid)
                _userData.value = userResponse
            } catch (e: Exception) {
                _userData.value = Result.failure(e)
            }
        }
    }
}
