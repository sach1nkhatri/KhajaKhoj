package com.example.khajakhoj.fragments

import CredentialManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.activity.SettingsActivity
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.LoginViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityProfileBinding
//    private val credentialManager: CredentialManager by lazy { CredentialManager(context = this) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingButtonProfile.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        binding.logOutProfile.setOnClickListener {
            Utils.logOut(requireContext())
            redirectToLoginPage()
        }

        viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Log.d("LoginViewModel", "User logged in: $user")
                Log.d("LoginViewModel", "User logged in: ${user.uid}")
                Log.d("LoginViewModel", "User logged in: ${user.email}")
                Log.d("LoginViewModel", "User logged in: ${user.fullName}")
                Log.d("LoginViewModel", "User logged in: ${user.address}")
                Log.d("LoginViewModel", "User logged in: ${user.createdAt}")

                val fullName = user.fullName
                val address = user.address
                val createdAt = user.createdAt

                // Update the UI with the fetched data
                binding.nameTextViewOnProfile.text = fullName
                binding.addressTextViewOnProfile.text = address

                val formattedDate = if (createdAt != 0L) {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(createdAt))
                } else {
                    "N/A"
                }
                binding.dateTextViewOnProfile.text = formattedDate

            } else {
                Log.d("LoginViewModel", "User not logged in")
            }
        })
    }

    private fun redirectToLoginPage() {
        startActivity(Intent(requireContext(), LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        requireActivity().finish()
    }


}