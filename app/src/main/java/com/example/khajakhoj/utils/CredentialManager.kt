package com.example.khajakhoj.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseUser


class CredentialManager(private val context: Context) {
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    init {
        initEncryptedSharedPreferences()
        initSharedPreferences()
    }

    private fun initEncryptedSharedPreferences() {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            context,
            "MyEncryptedPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    private fun initSharedPreferences() {
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    fun saveEncryptedCredentials(email: String, password: String) {
        with(encryptedSharedPreferences.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    fun removeEncryptedCredentials() {
        with(encryptedSharedPreferences.edit()) {
            remove("email")
            remove("password")
            apply()
        }
    }

    fun getEncryptedCredentials(): Pair<String?, String?> {
        val email = encryptedSharedPreferences.getString("email", "")
        val password = encryptedSharedPreferences.getString("password", "")
        return Pair(email, password)
    }


    fun saveSharedCredentials(user: User) {
        with(sharedPreferences.edit()) {
            putString("fullName", user.fullName)
            putString("email", user.email)
            putString("address", user.address)
            putLong("createdAt", user.createdAt)
            apply()
        }
    }


    fun clearSharedCredentials() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
