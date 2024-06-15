package com.example.khajakhoj.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.databinding.ActivitySignUpBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LocationUtils
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var locationUtils: LocationUtils
    private var isSignUpInProgress = false
    private var isActivityStarted = false

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationUtils.getLocation { address ->
                if (address != null) {
                    getLocationAndSignUp(address)
                }
            }
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationUtils = LocationUtils(this)

        setupSignUpButton()
        observeSignUpResult()

        binding.termsTextView.setOnClickListener {
            Utils.showTermsAndConditions(this)
        }

        binding.backLoginView.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
    }

    private fun setupSignUpButton() {
        binding.signupCustBtn.setOnClickListener {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLocationAndSignUp(address: String) {
        if (isSignUpInProgress) {
            return
        }
        isSignUpInProgress = true

        val fullName = binding.fullNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val phoneNumber = binding.phoneEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        viewModel.signUpUser(
            fullName,
            email,
            phoneNumber,
            password,
            confirmPassword,
            address
        )
    }

    private fun observeSignUpResult() {
        viewModel.signUpResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Sign-up successful! Please login.", Toast.LENGTH_SHORT).show()
                navigateToLoginPage()
            }.onFailure { exception ->
                val errorMessage = exception.message ?: "Unknown error occurred"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                isSignUpInProgress = false
            }
        })
    }

    private fun navigateToLoginPage() {
        if (!isActivityStarted) {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
            isActivityStarted = true
        }
    }
}
