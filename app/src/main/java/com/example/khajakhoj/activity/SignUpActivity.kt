package com.example.khajakhoj.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.databinding.ActivitySignUpBinding
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.utils.PasswordVisibilityToggler
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: UserViewModel by viewModels()

    private var isSignUpInProgress = false
    private var isActivityStarted = false
    private lateinit var loadingUtil: LoadingUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordToggle.setOnClickListener {
            PasswordVisibilityToggler.togglePasswordVisibility(
                binding.passwordEditText,
                binding.passwordToggle
            )
        }

        binding.confirmPasswordToggle.setOnClickListener {
            PasswordVisibilityToggler.togglePasswordVisibility(
                binding.confirmPasswordEditText,
                binding.confirmPasswordToggle
            )
        }

        loadingUtil = LoadingUtil(this)

        setupSignUpButton()
        observeSignUpResult()

        binding.termsTextView.setOnClickListener {
            Utils.showTermsAndConditions(this)
        }

        binding.backLoginView.setOnClickListener {
            Log.d("SignUpActivity", "Back to login clicked")
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    private fun setupSignUpButton() {
        binding.signupCustBtn.setOnClickListener {
            signUpUser()
            Log.d("SignUpActivity", "Sign-up button clicked")
        }
    }

    private fun signUpUser() {
        if (isSignUpInProgress) {
            Log.d("SignUpActivity", "Sign-up already in progress")
            return
        }

        val fullName = binding.fullNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val phoneNumber = binding.phoneEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        Log.d(
            "SignUpActivity",
            "Starting sign-up with details: FullName=$fullName, Email=$email, PhoneNumber=$phoneNumber"
        )

        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (!binding.termsCheckBox.isChecked) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()
            return
        }

        loadingUtil.showLoading()
        isSignUpInProgress = true

        viewModel.signUpUser(
            fullName,
            email,
            phoneNumber,
            password,
            confirmPassword
        )
    }

    private fun observeSignUpResult() {
        viewModel.signUpResult.observe(this, Observer { result ->
            loadingUtil.dismiss() // Dismiss loading indicator
            isSignUpInProgress = false

            result.onSuccess {
                Log.d("SignUpActivity", "Sign-up successful")
                Toast.makeText(this, "Sign-up successful! Please login.", Toast.LENGTH_SHORT).show()
                navigateToLoginPage()
            }.onFailure { exception ->
                val errorMessage = exception.message ?: "Unknown error occurred"
                Log.e("SignUpActivity", "Sign-up failed: $errorMessage")
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLoginPage() {
        if (!isActivityStarted) {
            Log.d("SignUpActivity", "Navigating to login page")
            startActivity(Intent(this, LoginPage::class.java))
            finish()
            isActivityStarted = true
        }
    }
}
