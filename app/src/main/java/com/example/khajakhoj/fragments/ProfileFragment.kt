package com.example.khajakhoj.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel

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
        val user: User? = arguments?.getParcelable("user")
        user?.let {
            binding.nameTextViewOnProfile.text = it.fullName
            binding.emailTextViewOnProfile.text = it.email
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
