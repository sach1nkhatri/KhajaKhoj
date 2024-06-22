package com.example.khajakhoj.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivityProfileBinding
import com.example.khajakhoj.databinding.ActivitySettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

}