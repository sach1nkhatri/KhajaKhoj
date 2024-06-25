package com.example.khajakhoj.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivityResDetailViewBinding
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.viewmodel.RestaurantViewModel
import com.squareup.picasso.Picasso

class ResDetailView : AppCompatActivity() {

    private lateinit var viewModel: RestaurantViewModel
    private lateinit var imageSwitcher: ImageSwitcher
    private lateinit var gestureDetector: GestureDetector
    private lateinit var binding: ActivityResDetailViewBinding
    private val imageIds = listOf(R.drawable.ad3, R.drawable.ad2, R.drawable.ad4)
    private var currentIndex = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        // Initialize ImageSwitcher
        imageSwitcher = findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory {
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView
        }

        // Initial image
        imageSwitcher.setImageResource(imageIds[currentIndex])

        // Setup gesture detection for swipe
        gestureDetector = GestureDetector(this, SwipeGestureListener())

        // Set OnTouchListener for swipe gestures on the CardView
        val cardView: CardView = findViewById(R.id.ResPhotos)
        cardView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

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

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")

        binding.bookmarkBtn.setOnClickListener {
            if (restaurant != null) {
                viewModel.bookmarkRestaurant(restaurant)
                observeBookmarkResult()
            }
        }

        Log.d("Restaurant", restaurant.toString())
        // Update UI with restaurant details
        restaurant?.let {
            binding.RestaurantName.text = it.name
            binding.ResturantCuisineDetail.text = it.cuisineType
            binding.RestaurantAddress.text = it.address
            binding.address.text = it.address
            binding.restaurantPhone.text = it.contactNumber
            binding.timing.text = "${it.openTime} - ${it.closeTime}"

            // Uncomment the following line if using an image loading library like Picasso or Glide
            // Picasso.get().load(it.restaurantLogoUrl).into(binding.restuarantImageLogo)

            if (it.bikeParking) {
                binding.twoWheelerParking.setImageResource(R.drawable.availabegreenicon)
            } else {
                binding.twoWheelerParking.setImageResource(R.drawable.wrongicon)
            }

            if (it.carParking) {
                binding.fourWheelerParking.setImageResource(R.drawable.availabegreenicon)
            } else {
                binding.fourWheelerParking.setImageResource(R.drawable.wrongicon)
            }

            if (it.wifi) {
                binding.wifi.setImageResource(R.drawable.availabegreenicon)
            } else {
                binding.wifi.setImageResource(R.drawable.wrongicon)
            }
        }
    }

    private fun observeBookmarkResult() {
        viewModel.bookmarkResult.observe(this, Observer { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(this, "Bookmark added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add bookmark: $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null || e2 == null) return false

            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

            return if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(
                    velocityX
                ) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (diffX > 0) {
                    onSwipeRight()
                } else {
                    onSwipeLeft()
                }
                true
            } else {
                false
            }
        }
    }

    private fun onSwipeLeft() {
        if (currentIndex < imageIds.size - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
        imageSwitcher.setImageResource(imageIds[currentIndex])
    }

    private fun onSwipeRight() {
        if (currentIndex > 0) {
            currentIndex--
        } else {
            currentIndex = imageIds.size - 1
        }
        imageSwitcher.setImageResource(imageIds[currentIndex])
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}