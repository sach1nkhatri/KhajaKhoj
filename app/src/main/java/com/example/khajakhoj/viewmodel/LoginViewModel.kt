//package com.example.khajakhoj.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.khajakhoj.model.User
//import com.example.khajakhoj.repository.LoginRepositoryImpl
//import kotlinx.coroutines.launch
//
//class LoginViewModel : ViewModel() {
//    private val repository = LoginRepositoryImpl()
//
//    private val _loginResponse = MutableLiveData<Result<Unit>>()
//    val loginResponse: LiveData<Result<Unit>> get() = _loginResponse
//
//
//
//     fun validateInputInfo(
//        email: String,
//        password: String,
//    ): Pair<Boolean, String> {
//        return try {
//            when {
////                emailCheck -> false to"Email already exists"
//                email.isEmpty() -> false to "Email cannot be empty"
//                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false to "Invalid email format"
//                password.isEmpty() -> false to "Password cannot be empty"
//                password.length < 6 -> false to "Password must be at least 6 characters long"
//                else -> true to ""
//            }
//        } catch (e: Exception) {
//            false to e.message.orEmpty()
//        }
//    }
//}
