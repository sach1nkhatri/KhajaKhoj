package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }
        binding.signup.setOnClickListener(){
            startActivity(Intent(this@LoginPage,SignUpActivity::class.java))
        }
    }

    private fun loginUser(username : String, password : String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithEmail:success")
//                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(baseContext, "No account with this email.", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(baseContext, "Incorrect password.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@LoginPage, Dashboard::class.java))
            finish()
        }
    }
}
