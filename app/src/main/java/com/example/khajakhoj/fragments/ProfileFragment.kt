package com.example.khajakhoj.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.activity.SettingsActivity
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityProfileBinding
    private var logoutDialog: androidx.appcompat.app.AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)


        // Set up button click listener to log out and redirect to the login page
        binding.logOutProfile.setOnClickListener {
            Utils.logOut(requireContext()) {
                // Callback for when logout is confirmed
                redirectToLoginPage()
            }.also {
                // Store reference to the dialog
                logoutDialog = it
            }
        }

        // Observe the current user data from ViewModel
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d("Profile Fragment", "User user: $user")
                Log.d("Profile Fragment", "uid: ${user.uid}")
                Log.d("Profile Fragment", "email: ${user.email}")
                Log.d("Profile Fragment", "createdAt: ${user.createdAt}")

                val fullName = user.fullName
                val createdAt = user.createdAt
//                val createdAt = 1719029919881L // Assuming this is your timestamp in milliseconds

                binding.nameTextViewOnProfile.text = fullName
                binding.emailTextViewOnProfile.text = user.email

                val formattedDate =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(createdAt))
                binding.dateTextViewOnProfile.text = formattedDate


            } else {
                Log.d("UserViewModel", "User not logged in")
            }
        }

        return binding.root
    }

    // Redirect to the login page and clear the fragment stack
    private fun redirectToLoginPage() {
        startActivity(Intent(requireContext(), LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        requireActivity().finish()
    }
}
