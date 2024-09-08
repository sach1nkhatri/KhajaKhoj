package com.example.khajakhoj.repository

interface AuthRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun signup(email: String, password: String, callback: (Boolean, String) -> Unit)
}
