package com.example.khajakhoj.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.khajakhoj.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Email Button Code Here
        val email = "lamibinod31@gmail.com"
        val subject = "Email Uri"
        val message = "This is the body of the email"

        val emailSend = binding.emailIcon
        emailSend.setOnClickListener {
            sendMail(email, subject, message)
        }

        val call = binding.phoneIcon
        call.setOnClickListener {
            callSomeone()
        }


    }

    private fun sendMail(email: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, email)// herer adresses is already string
            putExtra(Intent.EXTRA_SUBJECT, subject) // send the values as string
            putExtra(Intent.EXTRA_TEXT, message) // send the values as string
        }
        startActivity(intent)
    }

    private fun callSomeone() {
        val phoneNumber = "1234567890"
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        if (callIntent.resolveActivity(packageManager) != null) {
            startActivity(callIntent)
        } else {
            Toast.makeText(this, "No calling apps installed.", Toast.LENGTH_SHORT).show()
        }
    }


}