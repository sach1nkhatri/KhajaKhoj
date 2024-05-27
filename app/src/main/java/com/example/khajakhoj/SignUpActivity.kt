package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.databinding.ActivitySignUpBinding
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.signupCustBtn.setOnClickListener {
            validateAndSignUp(
                binding.fullNameEditText.text.toString().trim(),
                binding.emailEditText.text.toString().trim(),
                binding.phoneEditText.text.toString().trim(),
                binding.passwordEditText.text.toString().trim(),
                binding.confirmPasswordEditText.text.toString().trim(),
                binding.termsCheckBox.isChecked
            )
        }

        binding.backLoginView.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginPage::class.java))
            finish()
        }
    }

    private fun validateAndSignUp(
        fullName : String,
        email : String,
        phone : String,
        password : String,
        confirmPassword : String,
        termsChecked : Boolean
    ) {
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!termsChecked) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return
        }
        signUpUser(fullName, email, phone, password)
    }

    private fun signUpUser(fullName : String, email : String, phone : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserData(it.uid, fullName, email, phone)
                    }
                } else {
                    Toast.makeText(this, "Authentication failed : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(uid : String, fullName : String, email : String, phone : String) {
        val user = User(uid, fullName, email, phone)
        database.child("users").child(uid).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {e ->
                Toast.makeText(this, "Failed to register user : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}