package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            Log.i("Test Credentials","username: $username and Password : $password")

            val intent = Intent(this@LoginPage, Dashboard::class.java)
            startActivity(intent)

            finish()


        }
        binding.signup.setOnClickListener(){
            startActivity(Intent(this@LoginPage,SignUpActivity::class.java))
        }





    }
}
