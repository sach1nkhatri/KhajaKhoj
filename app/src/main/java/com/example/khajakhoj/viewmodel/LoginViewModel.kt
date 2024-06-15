package com.example.khajakhoj.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.User
import com.example.khajakhoj.repository.LoginRepository
import com.example.khajakhoj.repository.LoginRepositoryImpl
import com.example.khajakhoj.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoginRepository = LoginRepositoryImpl(application)

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> = _loginResult

    private val _resetPasswordResult = MutableLiveData<Result<Boolean>>()
    val resetPasswordResult: LiveData<Result<Boolean>> = _resetPasswordResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUserWithEmailPassword(email, password)
            if (result is Result.Success && result.data) {
                val user = repository.getCurrentUser()
                _currentUser.postValue(user)
            }
            _loginResult.postValue(result)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _resetPasswordResult.postValue(repository.sendPasswordResetEmail(email))
        }
    }

    fun getCurrentUser() {
        _currentUser.postValue(repository.getCurrentUser())
    }
}
