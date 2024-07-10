package com.example.khajakhoj.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityProfileBinding
    private var logoutDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up button click listener to log out and redirect to the login page
        binding.logOutProfile.setOnClickListener {
            Utils.logOut(this) {
                // Callback for when logout is confirmed
                redirectToLoginPage()
            }.also {
                // Store reference to the dialog
                logoutDialog = it
            }
        }

        // Observe the current user data from ViewModel
        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                Log.d("LoginViewModel", "User logged in: $user")
                Log.d("LoginViewModel", "User logged in: ${user.uid}")
                Log.d("LoginViewModel", "User logged in: ${user.email}")
                Log.d("LoginViewModel", "User logged in: ${user.fullName}")
                Log.d("LoginViewModel", "User logged in: ${user.createdAt}")

                val fullName = user.fullName
                val createdAt = user.createdAt

                // Update the UI with the fetched data
                binding.nameTextViewOnProfile.text = fullName

                val formattedDate = if (createdAt != 0L) {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(createdAt))
                } else {
                    "N/A"
                }
                binding.dateTextViewOnProfile.text = formattedDate

            } else {
                Log.d("LoginViewModel", "User not logged in")
            }
        }

        // Add LifecycleObserver to manage the dialog lifecycle
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                logoutDialog?.dismiss()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                logoutDialog?.dismiss()
            }
        })
    }

    // Redirect to the login page and clear the activity stack
    private fun redirectToLoginPage() {
        startActivity(Intent(this, LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}
