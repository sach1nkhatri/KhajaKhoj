//package com.example.learningapp.ui.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.khajakhoj.repository.SignupRepository
//import com.google.i18n.phonenumbers.PhoneNumberUtil
//import kotlinx.coroutines.launch
//
//class SignUpViewModel(
//    private val signupRepository: SignupRepository = SignupRepositoryImpl()
//) : ViewModel() {
//
//    private val _signUpResponse = MutableLiveData<Result<Unit>>()
//    val signUpResponse: LiveData<Result<Unit>> get() = _signUpResponse
//
//    fun signUp(fullName: String, email: String, address : String, phoneNumber: String, password: String, confirmPassword: String) {
//        if (!validateInput(fullName, email, address, phoneNumber, password, confirmPassword)) return
//
//        viewModelScope.launch {
//            try {
//                val emailExists = signupRepository.checkEmailExists(email)
//                if (emailExists) {
//                    _signUpResponse.value = Result.failure(Exception("Email already exists"))
//                } else {
//                    val signUpResponse = signupRepository.signUpUser(email, password)
//                    if (signUpResponse.isSuccess) {
//                        val uid = signUpResponse.getOrNull() ?: return@launch
//                        val saveResult = signupRepository.saveUserData(uid, fullName, email, phoneNumber, address)
//                        _signUpResponse.value = saveResult
//                    } else {
//                        _signUpResponse.value = Result.failure(
//                            signUpResponse.exceptionOrNull() ?: Exception("Unknown error")
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _signUpResponse.value = Result.failure(e)
//            }
//        }
//    }
//
//    private fun validateInput(
//        fullName: String,
//        email: String,
//        phoneNumber: String,
//        password: String,
//        confirmPassword: String,
//        confirmPassword1: String
//    ): Boolean {
//        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//            _signUpResponse.value = Result.failure(Exception("Please fill in all fields"))
//            return false
//        }
//
//        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _signUpResponse.value = Result.failure(Exception("Invalid email address"))
//            return false
//        }
//
//        if (!validatePhoneNumber(phoneNumber)) {
//            _signUpResponse.value = Result.failure(Exception("Invalid phone number"))
//            return false
//        }
//
//        if (password.length < 6) {
//            _signUpResponse.value =
//                Result.failure(Exception("Password must be at least 6 characters"))
//            return false
//        }
//
//        if (password != confirmPassword) {
//            _signUpResponse.value = Result.failure(Exception("Passwords do not match"))
//            return false
//        }
//
//        return true
//    }
//
//    private fun validatePhoneNumber(phoneNumber: String): Boolean {
//        return try {
//            val phoneUtil = PhoneNumberUtil.getInstance()
//            val phoneNumberObj = phoneUtil.parse(phoneNumber, "NP")
//            phoneUtil.isValidNumber(phoneNumberObj)
//        } catch (e: Exception) {
//            false
//        }
//    }
//}
