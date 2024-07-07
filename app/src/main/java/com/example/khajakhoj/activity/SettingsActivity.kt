package com.example.khajakhoj.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.khajakhoj.databinding.ActivitySettingsBinding
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isDarkModeOn = sharedPreferences.getBoolean("DarkMode", false)

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.privacyButton.setOnClickListener(){
            Utils.showPolicy(this)
        }

        binding.darkMode.isChecked = isDarkModeOn

        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("DarkMode", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("DarkMode", false)
            }
            editor.apply()
        }

        // Initialize SharedPreferences editor
        editor = sharedPreferences.edit()
        binding.changePasswordButton.setOnClickListener(){
            Utils.showPasswordChangeDialog(this,userViewModel)
        }
        binding.settingImageView.setOnClickListener(){
            startActivity(Intent(this@SettingsActivity, ProfileActivity::class.java))
        }

        }


}