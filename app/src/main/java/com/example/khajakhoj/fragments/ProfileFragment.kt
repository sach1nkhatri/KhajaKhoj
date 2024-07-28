package com.example.khajakhoj.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityProfileBinding
    private var logoutDialog: androidx.appcompat.app.AlertDialog? = null
    private var profileImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)

        binding.imageView3.setOnClickListener {
            openGallery()
        }

        viewModel.imageUploadResult.observe(viewLifecycleOwner, Observer { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(context, "Image upload successful!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })

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
            val profileImage = user.profilePictureUrl
            if (!profileImage.isNullOrEmpty()) {
                Picasso.get().load(profileImage).into(binding.imageView3)
            } else {
                binding.imageView3.setImageResource(R.drawable.pizzaico)
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                profileImageUri = data?.data
                viewModel.updateUserProfileImage(profileImageUri!!)
                Log.d("Profile Logo Uri", "$profileImageUri")
            }
        }
}
