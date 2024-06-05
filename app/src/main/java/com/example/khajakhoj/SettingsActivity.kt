package com.example.khajakhoj

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.databinding.ActivityCouponsBinding
import com.example.khajakhoj.databinding.ActivitySettingsBinding
import com.example.khajakhoj.utils.Utils

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
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
            Utils.showPasswordChangeDialog(this)
        }
        binding.settingImageView.setOnClickListener(){
            startActivity(Intent(this@SettingsActivity,ProfileActivity::class.java))
        }

        }


}