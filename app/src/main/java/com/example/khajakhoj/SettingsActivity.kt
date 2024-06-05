package com.example.khajakhoj

import android.app.Dialog
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
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.change_password_dialog)
            dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
            dialog.show()
        }

        }


}