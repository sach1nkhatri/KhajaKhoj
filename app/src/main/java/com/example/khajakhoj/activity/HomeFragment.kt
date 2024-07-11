package com.example.khajakhoj.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var imageSwitcher: ImageSwitcher
    private val imageIds = arrayOf(R.drawable.ad1, R.drawable.ad2, R.drawable.ad3, R.drawable.ad4)
    private var currentIndex = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ImageSwitcher using view binding
        imageSwitcher = binding.imageSwitcher
        imageSwitcher.setFactory(ViewSwitcher.ViewFactory {
            ImageView(requireContext()).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        })

        // Initial image
        imageSwitcher.setImageResource(imageIds[currentIndex])

        // Setup handler to switch images
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                currentIndex = (currentIndex + 1) % imageIds.size
                imageSwitcher.setImageResource(imageIds[currentIndex])
                handler.postDelayed(this, 5000) // Switch image every 5 seconds
            }
        }
        handler.postDelayed(runnable, 5000) // Start the image switcher

        // Set click listener on image buttons
        setupCuisineButtons()

        return view
    }

    private fun setupCuisineButtons() {
        binding.apply {
            Nepali.setOnClickListener { startRestaurantViewActivity("Nepali") }
            Newari.setOnClickListener { startRestaurantViewActivity("Newari") }
            Italian.setOnClickListener { startRestaurantViewActivity("Italian") }
            American.setOnClickListener { startRestaurantViewActivity("American") }
            Indian.setOnClickListener { startRestaurantViewActivity("Indian") }
            Japanese.setOnClickListener { startRestaurantViewActivity("Japanese") }
        }
    }

    private fun startRestaurantViewActivity(cuisineType: String) {
        val intent = Intent(requireActivity(), RestaurantView::class.java)
        intent.putExtra("CUISINE_TYPE", cuisineType)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable) // Stop the image switcher when the fragment is destroyed
    }
}
