package com.example.khajakhoj

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSharedPreferences()
        displayUserData()

        binding.settingButtonProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, SettingsActivity::class.java))
        }

        binding.logOutProfile.setOnClickListener {
            Utils.logOut(this@ProfileActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    private fun displayUserData() {
        val fullName = sharedPreferences.getString("fullName", "")
        val address = sharedPreferences.getString("address", "")
        val createdAt = sharedPreferences.getLong("createdAt", 0L)

        // Update the UI with the fetched data
        binding.nameTextViewOnProfile.text = fullName
        binding.addressTextViewOnProfile.text = address

        val formattedDate = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(createdAt))
        binding.dateTextViewOnProfile.text = formattedDate
    }
}
