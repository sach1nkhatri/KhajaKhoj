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
        val userCredentials = credentialManager.getSavedCredentials()

        val fullName = userCredentials?.fullName
        val address = userCredentials?.address
        val createdAt =userCredentials?.createdAt // Provide a default value of 0L if createdAt is null
        Log.d("ProfileActivity", "CreatedAt=$createdAt")

        // Update the UI with the fetched data
        binding.nameTextViewOnProfile.text = fullName
        binding.addressTextViewOnProfile.text = address

        if (createdAt != 0L) {
            val formattedDate =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(createdAt?.let { Date(it) })
            binding.dateTextViewOnProfile.text = formattedDate
        } else {
            binding.dateTextViewOnProfile.text = "N/A"
        }

        Log.d(
            "ProfileActivity",
            "Displayed user credentials: FullName=$fullName, Address=$address, CreatedAt=$createdAt"
        )
    }


    private fun redirectToLoginPage() {
        startActivity(Intent(this, LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}
