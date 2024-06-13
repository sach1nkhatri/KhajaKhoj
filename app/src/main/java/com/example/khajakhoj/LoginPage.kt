package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.databinding.ActivityLoginPageBinding
import com.example.khajakhoj.utils.Utils.showForgotPasswordDialog
import com.example.khajakhoj.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private val viewmodel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        observeViewModel()
        setupLoginScreen()
    }

    private fun setupLoginScreen() {
        binding.loginBtn.setOnClickListener {
            handleLogin()
        }
        binding.signup.setOnClickListener {
            startActivity(Intent(this@LoginPage, SignUpActivity::class.java))
        }
        binding.forgotpasswordBtn.setOnClickListener {
            showForgotPasswordDialog(this) { email ->
                viewmodel.sendPasswordResetEmail(email)
            }
        }
    }

    private fun observeViewModel() {
        viewmodel.userResponse.observe(this, Observer { result ->
            result?.let {
                if (it.isSuccess) {
                    it.getOrNull()?.let {
                        if (FirebaseAuth.getInstance().currentUser != null) {
                            startActivity(Intent(this@LoginPage, Dashboard::class.java))
                            finish()
                        }                    }
                } else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewmodel.passwordResetResult.observe(this, Observer { result ->
            result?.let {
                if (it.isSuccess) {
                    Toast.makeText(this, "Password reset email sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error sending password reset email", Toast.LENGTH_SHORT).show()                }
            }
        })
    }


    private fun handleLogin() {
        val email = binding.usernameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                binding.usernameInput.error = "Please enter your email"
            }
            if (password.isEmpty()) {
                binding.passwordInput.error = "Please enter your password"
            }
        } else {
            viewmodel.signIn(email, password)
        }
    }
}