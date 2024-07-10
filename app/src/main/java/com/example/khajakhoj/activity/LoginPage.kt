package com.example.khajakhoj.activity

import CredentialManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.databinding.ActivityLoginPageBinding
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.utils.Utils.showForgotPasswordDialog
import com.example.khajakhoj.viewmodel.UserViewModel

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var loadingUtil: LoadingUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtil = LoadingUtil(this)


        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            if (validateInput(email, password)) {
                loadingUtil.showLoading()
                viewModel.loginUser(email, password)
            }
        }

        binding.forgotpasswordBtn.setOnClickListener {
            showForgotPasswordDialog(this) { email ->
                viewModel.sendPasswordResetEmail(email)
            }
        }

        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    val TAG = "Login Activity"

    private fun observeViewModel() {
        viewModel.loginResult.observe(this, Observer { loginResult ->
            loadingUtil.dismiss() // Dismiss loading indicator
            if (loginResult.isSuccess) {
                // Login successful
                Log.d(TAG, "Sign-in successful from Activity")
                val credentialManager = CredentialManager(this)
                credentialManager.saveLoginState(true)
                // Handle successful login, e.g., navigate to another activity
                startActivity(Intent(this, Dashboard::class.java))
            } else {
                // Login failed
                Log.e(TAG, "Sign-in failed from Activity")
                // Handle login failure, e.g., display an error message
                Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.resetPasswordResult.observe(this, Observer { resetResult ->
            if (resetResult.isSuccess) {
                // Password reset email sent successfully (likely handled in toast message)
            } else {
                // Handle any errors during password reset (already handled most likely)
            }
        })

        // Observe toast messages from the view model
        viewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.usernameInput.error = "Please enter your email"
                false
            }
            password.isEmpty() -> {
                binding.passwordInput.error = "Please enter your password"
                false
            }
            else -> true
        }
    }
}
