package com.example.khajakhoj.utils

import android.util.Log
import com.google.i18n.phonenumbers.PhoneNumberUtil

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePhoneNumber(phoneNumber: String): Boolean {
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