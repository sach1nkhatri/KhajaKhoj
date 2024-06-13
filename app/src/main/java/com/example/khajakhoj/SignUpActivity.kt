package com.example.khajakhoj

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.khajakhoj.databinding.ActivitySignUpBinding
import com.example.khajakhoj.utils.LocationUtils
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var locationUtils: LocationUtils
    private var isSignUpInProgress = false //
    private var isActivityStarted = false //

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLocationAndSignUp()
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

//    private fun getLocationAndSignUp() {
//        locationUtils.getLocation { address ->
//            if (address != null) {
//                val fullName = binding.fullNameEditText.text.toString().trim()
//                val email = binding.emailEditText.text.toString().trim()
//                val phoneNumber = binding.phoneEditText.text.toString().trim()
//                val password = binding.passwordEditText.text.toString().trim()
//                val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
//
//                viewModel.signUpUser(fullName, email, phoneNumber, address, password, confirmPassword)
//            } else {
//                Toast.makeText(this, "Failed to retrieve location address", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun getLocationAndSignUp() {
        // Check if sign-up is already in progress
        if (isSignUpInProgress) {
            return  // Ignore the click if sign-up is already in progress
        }
        // Set sign-up in progress flag to true
        isSignUpInProgress = true

        var address = "Kathmandu"
        val fullName = binding.fullNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val phoneNumber = binding.phoneEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

        viewModel.signUpUser(fullName, email, phoneNumber, address, password, confirmPassword)
    }

    private fun observeSignUpResult() {
        viewModel.userResponse.observe(this) { result ->
            if (result.isSuccess && !isActivityStarted) {
                startActivity(Intent(this, LoginPage::class.java))
                finish()
                isActivityStarted = true
                Toast.makeText(this, "Sign-up successful! Please login.", Toast.LENGTH_SHORT).show()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }

            // Reset sign-up in progress flag after handling sign-up result
            isSignUpInProgress = false
        }
    }
}
