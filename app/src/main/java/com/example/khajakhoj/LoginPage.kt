package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginPage : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private  lateinit var loginBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)


        usernameInput= findViewById(R.id.username_input)
        passwordInput= findViewById(R.id.password_input)
        loginBtn= findViewById(R.id.login_Btn)
        loginBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            Log.i("Test Credentials","username: $username and Password : $password")

            val intent = Intent(this@LoginPage, Dashboard::class.java)
            startActivity(intent)


        }




    }
}