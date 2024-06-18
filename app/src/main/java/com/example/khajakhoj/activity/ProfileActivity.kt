package com.example.khajakhoj.activity

import CredentialManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val credentialManager: CredentialManager by lazy { CredentialManager(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayUserData()

        binding.settingButtonProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, SettingsActivity::class.java))
        }

        binding.logOutProfile.setOnClickListener {
            Utils.logOut(this@ProfileActivity)
            redirectToLoginPage()
        }
    }

    private fun displayUserData() {
        val userCredentials = credentialManager.getUser()

        if (userCredentials != null) {
            val fullName = userCredentials.fullName
            val address = userCredentials.address
            val createdAt = userCredentials.createdAt // Default value if createdAt is null is not needed here since it is a non-nullable long
            Log.d("ProfileActivity", "CreatedAt=$createdAt")

            // Update the UI with the fetched data
            binding.nameTextViewOnProfile.text = fullName
            binding.addressTextViewOnProfile.text = address

            val formattedDate = if (createdAt != 0L) {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(createdAt))
            } else {
                "N/A"
            }
            binding.dateTextViewOnProfile.text = formattedDate

            Log.d("ProfileActivity", "Displayed user credentials: FullName=$fullName, Address=$address, CreatedAt=$createdAt")
        } else {
            Log.d("ProfileActivity", "User credentials not found")
            binding.nameTextViewOnProfile.text = "N/A"
            binding.addressTextViewOnProfile.text = "N/A"
            binding.dateTextViewOnProfile.text = "N/A"
        }
    }

    private fun redirectToLoginPage() {
        startActivity(Intent(this, LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}
