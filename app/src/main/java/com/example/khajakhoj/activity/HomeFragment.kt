package com.example.khajakhoj.activity

import AdsViewModel
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.SearchView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.FragmentHomeBinding
import com.example.khajakhoj.test.ImagePagerAdapter

import android.widget.ProgressBar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImagePagerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressBar: ProgressBar
    private lateinit var adsViewModel: AdsViewModel
    lateinit var cusineSearch : ImageView

    private val autoSwipeRunnable = object : Runnable {
        override fun run() {
            val nextItem = (viewPager.currentItem + 1) % adapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 5000) // Adjust the delay as needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewPager = binding.viewPager
        progressBar = binding.progressBar
        adsViewModel = ViewModelProvider(this).get(AdsViewModel::class.java)

        adsViewModel.adsList.observe(viewLifecycleOwner, Observer { imageUrls ->
            progressBar.visibility = View.GONE
            if (imageUrls.isNotEmpty()) {
                adapter = ImagePagerAdapter(imageUrls)
                viewPager.adapter = adapter
                binding.springDotsIndicatorHome.attachTo(viewPager)
                startAutoSwipe()
            }
        })

        cusineSearch = view.findViewById(R.id.cuisineSearch)
        fetchAds()

        // Set click listener on image buttons
        setupCuisineButtons()

        cusineSearch.setOnClickListener {
            val intent = Intent(requireContext(), GlobalSearchView::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun fetchAds() {
        progressBar.visibility = View.VISIBLE
        adsViewModel.fetchAds()
    }

    private fun startAutoSwipe() {
        handler.postDelayed(autoSwipeRunnable, 3000) // Adjust the delay as needed
    }

    private fun stopAutoSwipe() {
        handler.removeCallbacks(autoSwipeRunnable)
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
        stopAutoSwipe()
    }
}
