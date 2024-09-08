package com.example.khajakhoj.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivitySettingsBinding
import com.example.khajakhoj.model.User
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class SettingsFragment : Fragment() {
    lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivitySettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.darkMode.isChecked = sharedPreferences.getBoolean("isDarkMode", false)

        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkMode", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkMode", false)
            }
            editor.apply()
            requireActivity().recreate()

        }


        binding.privacyButton.setOnClickListener {
            Utils.showPolicy(requireContext())
        }



        binding.changePasswordButton.setOnClickListener {
            Utils.showPasswordChangeDialog(requireContext(),viewModel,
                LoadingUtil(requireActivity())
            )
        }

//        binding.settingImageView.setOnClickListener {
//
//            val user: User? = arguments?.getParcelable("user")
//            user?.let {
//                val profileImage = user.profilePictureUrl
//                if (!profileImage.isNullOrEmpty()) {
//                    Picasso.get().load(profileImage).into(binding.settingImageView)
//                } else {
//                    binding.settingImageView.setImageResource(R.drawable.pizzaico)
//                }
//            }
//
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.frameLayout, ProfileFragment())
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }

        binding.deleteAccountButton.setOnClickListener(){
            Utils.showDeleteAccountDialog(requireContext(),viewModel,LoadingUtil(requireActivity()))
        }
        binding.languageOption.setOnClickListener(){
            Toast.makeText(requireContext(),"English is the only available language for now.",Toast.LENGTH_LONG).show()
        }
    }
}
