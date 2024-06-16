package com.example.khajakhoj.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.databinding.ActivityLoginPageBinding
import com.example.khajakhoj.utils.Result
import com.example.khajakhoj.utils.Utils.showForgotPasswordDialog
import com.example.khajakhoj.viewmodel.LoginViewModel

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            if (validateInput(email, password)) {
                viewModel.signInUser(email, password)
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

    private fun observeViewModel() {
        viewModel.loginResult.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                }

                is Result.Failure -> {
                    Toast.makeText(
                        this,
                        result.exception.message ?: "Unknown error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {
                    // Optionally show a progress indicator
                }
            }
        })

        viewModel.resetPasswordResult.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(
                        this,
                        "Password reset email sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Failure -> {
                    Toast.makeText(
                        this,
                        result.exception.message ?: "Unknown error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {
                    // Optionally show a progress indicator
                }
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
