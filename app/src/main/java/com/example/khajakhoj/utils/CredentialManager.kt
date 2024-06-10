package com.example.khajakhoj.utils

object CredentialManager {
//    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
//    private lateinit var masterKey: MasterKey
//
//    fun storeCredentials(context: Context, email: String, password: String) {
//        val masterKey = MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
//            context,
//            "credentials",
//            masterKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//
//        val editor = encryptedSharedPreferences.edit()
//        editor.putString("email", email)
//        editor.putString("password", password)
//        editor.apply()
//    }
//
//    fun checkStoredCredentials(context: Context): Pair<String?, String?> {
//        val masterKey = MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
//            context,
//            "credentials",
//            masterKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//
//        val email = encryptedSharedPreferences.getString("email", null)
//        val password = encryptedSharedPreferences.getString("password", null)
//
//        return Pair(email, password)
//    }
}