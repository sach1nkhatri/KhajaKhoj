package com.example.khajakhoj.activity

import CredentialManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.khajakhoj.R

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var progressStatus = 0
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Initialize progress bar
        progressBar = findViewById(R.id.progressBar)

        credentialManager = CredentialManager(this)

        // Start updating progress after a delay
        Handler().postDelayed({
            updateProgress()
        }, 10)
    }

    private fun updateProgress() {
        val thread = Thread {
            while (progressStatus < 100) {
                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressStatus++
                progressBar.progress = progressStatus
            }
            // When progress reaches 100%, determine where to navigate
            if (credentialManager.isLoggedIn()) {
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                Log.d("Main Activity", "isLoggedIn: true")
                navigateToDashboard()
            } else {
                navigateToLogin()
            }
        }
        thread.start()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@MainActivity, Dashboard::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this@MainActivity, LoginPage::class.java)
        startActivity(intent)
        finish()
    }
}
