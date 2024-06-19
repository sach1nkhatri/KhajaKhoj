package com.example.khajakhoj.testActivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivityTestBinding
import com.example.khajakhoj.viewmodel.LoginViewModel
import org.jetbrains.skia.impl.Log

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private val viewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}