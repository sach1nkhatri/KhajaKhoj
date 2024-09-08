package com.example.khajakhoj.utils

import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePhoneNumber(phoneNumber: String, defaultRegion: String = "NP"): Boolean {
        return try {
            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            val number = phoneNumberUtil.parse(phoneNumber, defaultRegion)
            phoneNumberUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            false
        }
    }
}