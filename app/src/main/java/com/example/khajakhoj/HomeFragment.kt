package com.example.khajakhoj

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
import com.example.khajakhoj.activity.RestaurantView
import com.example.khajakhoj.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView
        })

        // Initial image
        imageSwitcher.setImageResource(imageIds[currentIndex])

        // Setup handler to switch images
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                currentIndex = (currentIndex + 1) % imageIds.size
                imageSwitcher.setImageResource(imageIds[currentIndex])
                handler.postDelayed(this, 5000) // Switch image every 3 seconds
            }
        }
        handler.postDelayed(runnable, 5000) // Start the image switcher

        // Set click listener on image buttons
        binding.Nepali.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "Nepali")
            startActivity(intent)
        }

        binding.Newari.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "Newari")
            startActivity(intent)
        }

        binding.Italian.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "Italian")
            startActivity(intent)
        }

        binding.American.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "American")
            startActivity(intent)
        }

        binding.Indian.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "Indian")
            startActivity(intent)
        }

        binding.Japanese.setOnClickListener {
            val intent = Intent(activity, RestaurantView::class.java)
            intent.putExtra("cuisine", "Japanese")
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable) // Stop the image switcher when the fragment is destroyed
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}