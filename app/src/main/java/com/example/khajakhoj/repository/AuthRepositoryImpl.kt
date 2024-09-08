package com.example.khajakhoj.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    callback(false, "Login failed: ${task.exception?.message}")
                }
            }
    }


    override fun signup(email: String, password: String, callback: (Boolean, String) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Signup successful")
                } else {
                    callback(false, task.exception?.message ?: "Signup failed")
                }
            }
    }
}
