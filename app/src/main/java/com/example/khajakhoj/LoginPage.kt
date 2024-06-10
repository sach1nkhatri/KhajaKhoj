package com.example.khajakhoj

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.khajakhoj.databinding.ActivityLoginPageBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Utils.showForgotPasswordDialog
import com.example.khajakhoj.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var encryptedSharedPreferences: EncryptedSharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        initEncryptedSharedPreferences()
        initSharedPreferences()

        if (checkSavedCredentials()) {
            Log.d("LoginPage", "Saved credentials found, attempting login...")
            startActivity(Intent(this@LoginPage, Dashboard::class.java))
            finish()
        } else {
            setupLoginScreen()
        }

        loginViewModel.userData.observe(this, Observer { result ->
            result?.let {
                if (it.isSuccess) {
                    it.getOrNull()?.let { user ->
                        saveUserData(user)
                        updateUI(auth.currentUser)
                    }
                } else {
                    handleLoginError(it.exceptionOrNull())
                }
            }
        })
    }

    private fun initEncryptedSharedPreferences() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            this,
            "MyEncryptedPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    private fun checkSavedCredentials(): Boolean {
        val savedEmail = encryptedSharedPreferences.getString("email", "")
        val savedPassword = encryptedSharedPreferences.getString("password", "")
        return if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            loginViewModel.loginUser(savedEmail, savedPassword)
            true
        } else {
            false
        }
    }

    private fun setupLoginScreen() {
        binding.loginBtn.setOnClickListener {
            handleLogin()
        }
        binding.signup.setOnClickListener {
            startActivity(Intent(this@LoginPage, SignUpActivity::class.java))
        }

        binding.forgotpassword.setOnClickListener {
            showForgotPasswordDialog(this)
        }

        loginViewModel.loginResponse.observe(this, Observer { result ->
            result?.let {
                if (it.isSuccess) {
                    val user = auth.currentUser
                    user?.let {
                        loginViewModel.fetchUserData(it.uid)
                    }
                } else {
                    handleLoginError(it.exceptionOrNull())
                }
            }
        })
    }

    private fun handleLogin() {
        val email = binding.usernameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            loginViewModel.loginUser(email, password)
            handleRememberMe(email, password, true)
        }
    }

    private fun saveUserData(user: User) {
        with(sharedPreferences.edit()) {
            putString("fullName", user.fullName)
            putString("email", user.email)
            putString("address", user.address)
            putLong("createdAt", user.createdAt)

            // Log the user data before applying the changes
            Log.d("UserData", "Full Name: ${user.fullName}")
            Log.d("UserData", "Email: ${user.email}")
            Log.d("UserData", "Address: ${user.address}")
            Log.d("UserData", "Created At: ${user.createdAt}")

            apply()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.d("LoginPage", "User is logged in, navigating to Dashboard...")
            startActivity(Intent(this@LoginPage, Dashboard::class.java))
            finish()
        } else {
            Log.d("LoginPage", "User is null, staying on LoginPage")
        }
    }

    private fun handleRememberMe(email: String, password: String, remember: Boolean) {
        with(encryptedSharedPreferences.edit()) {
            if (remember) {
                putString("email", email)
                putString("password", password)
            } else {
                remove("email")
                remove("password")
            }
            apply()
        }
    }

    private fun handleLoginError(exception: Throwable?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> {
                Toast.makeText(baseContext, "No account with this email.", Toast.LENGTH_SHORT).show()
            }

            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(baseContext, "Incorrect password.", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(
                    baseContext,
                    "Authentication failed: ${exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        updateUI(null)
    }
}
