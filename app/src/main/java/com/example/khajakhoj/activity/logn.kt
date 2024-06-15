//package com.example.khajakhoj.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import com.example.khajakhoj.databinding.ActivityLoginPageBinding
//import com.example.khajakhoj.utils.Utils
//import com.example.khajakhoj.viewmodel.UserViewModel
//
//class LoginPage : AppCompatActivity() {
//    private lateinit var binding: ActivityLoginPageBinding
//    private val viewModel: UserViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginPageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        enableEdgeToEdge()
//
//        observeViewModel()
//        setupLoginScreen()
//    }
//
//    private fun setupLoginScreen() {
//        binding.loginBtn.setOnClickListener {
//            handleLogin()
//        }
//        binding.signup.setOnClickListener {
//            startActivity(Intent(this@LoginPage, SignUpActivity::class.java))
//            finish()
//        }
//        binding.forgotpasswordBtn.setOnClickListener {
//            Utils.showForgotPasswordDialog(this) { email ->
//                viewModel.sendPasswordResetEmail(email)
//            }
//        }
//    }
//
//    private fun observeViewModel() {
//        viewModel.userResponse.observe(this, Observer { result ->
//            if (result != null) {
//                if (result.isSuccess) {
//                    navigateToDashboard()
//                } else {
//                    handleLoginError(result.exceptionOrNull())
//                }
//            }
//        })
//
//        viewModel.passwordResetResult.observe(this, Observer { result ->
//            if (result != null) {
//                if (result.isSuccess) {
//                    Toast.makeText(
//                        this,
//                        "Password reset email sent successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    val errorMessage = when (result.exceptionOrNull()) {
//                        is Error -> result.exceptionOrNull()?.message
//                        else -> "Error sending password reset email"
//                    }
//                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//    }
//
//    private fun navigateToDashboard() {
//        startActivity(Intent(this@LoginPage, Dashboard::class.java))
//        finish()
//    }
//
//    private fun handleLoginError(exception: Throwable?) {
//        val errorMessage = when (exception) {
//            is UserViewModel.NoAccountException -> "No account with this email"
//            is UserViewModel.InvalidPasswordException -> "Invalid password"
//            is UserViewModel.UnknownErrorException -> "Login failed: Unknown error"
//            else -> "Login failed: ${exception?.message ?: "Unknown error"}"
//        }
//        Log.e("LoginPage", "Login failed: $errorMessage")
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//    }
//
//
//    private fun handleLogin() {
//        val email = binding.usernameInput.text.toString().trim()
//        val password = binding.passwordInput.text.toString().trim()
//
//        if (email.isEmpty() || password.isEmpty()) {
//            showValidationError(email, password)
//        } else {
//            viewModel.signIn(email, password)
//        }
//    }
//
//    private fun showValidationError(email: String, password: String) {
//        when {
//            email.isEmpty() -> Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
//            password.isEmpty() -> Toast.makeText(
//                this,
//                "Please enter your password",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//}