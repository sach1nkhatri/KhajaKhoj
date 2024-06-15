package com.example.khajakhoj.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.khajakhoj.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
    private lateinit var supportBinding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportBinding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(supportBinding.root)


    }


}