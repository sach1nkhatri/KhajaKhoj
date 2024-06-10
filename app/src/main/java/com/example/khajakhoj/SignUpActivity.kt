package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.khajakhoj.databinding.ActivitySignUpBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.Utils
import com.example.learningapp.ui.viewmodel.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel : SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        setupSignUpButton()
        observeSignUpResult()
    }

    private fun setupSignUpButton() {
        binding.signupCustBtn.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phoneNumber = binding.phoneEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            viewModel.signUp(fullName, email, phoneNumber, password, confirmPassword)
        }
    }

    private fun observeSignUpResult() {
        viewModel.signUpResponse.observe(this) { result ->
            if (result.isSuccess) {
                startActivity(Intent(this, LoginPage::class.java))
                finish()
                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}