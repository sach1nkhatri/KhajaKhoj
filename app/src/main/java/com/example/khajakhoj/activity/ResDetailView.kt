package com.example.khajakhoj.activity

import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.ReviewAdapter
import com.example.khajakhoj.databinding.ActivityResDetailViewBinding
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.model.Review
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
        if (restaurant != null) {
            val restaurantNameTextView: TextView = binding.RestaurantName
            restaurantNameTextView.text = if (restaurant.name.length > 15) {
                "${restaurant.name.substring(0, 15)}..."
            } else {
                restaurant.name
            }

            val restaurantCuisineTextView: TextView = binding.ResturantCuisineDetail
            restaurantCuisineTextView.text = restaurant.cuisineType

            val restaurantAddressTextView1: TextView = binding.RestaurantAddress
            restaurantAddressTextView1.text = restaurant.address

            val restaurantAddressTextView2: TextView = binding.visit
            if (restaurant.address.length > 13) {
                restaurantAddressTextView2.text = "${restaurant.address.substring(0, 13)}..."
            } else {
                restaurantAddressTextView2.text = restaurant.address
            }

            val restaurantPhoneTextView: TextView = binding.restaurantPhone
            restaurantPhoneTextView.text = restaurant.contactNumber

            val restaurantTimingTextView: TextView = binding.timing
            restaurantTimingTextView.text = "${restaurant.openTime} - ${restaurant.closeTime}"

            val twoWheelerParkingAvailability: ImageView = binding.twoWheelerParking
            if (restaurant.bikeParking) {
                twoWheelerParkingAvailability.setImageResource(R.drawable.availabegreenicon)
            } else {
                twoWheelerParkingAvailability.setImageResource(R.drawable.wrongicon)
            }

            val fourWheelerParkingAvailability: ImageView = binding.fourWheelerParking
            if (restaurant.carParking) {
                fourWheelerParkingAvailability.setImageResource(R.drawable.availabegreenicon)
            } else {
                fourWheelerParkingAvailability.setImageResource(R.drawable.wrongicon)
            }

            val wifiAvailability: ImageView = binding.wifi
            if (restaurant.wifi) {
                wifiAvailability.setImageResource(R.drawable.availabegreenicon)
            } else {
                wifiAvailability.setImageResource(R.drawable.wrongicon)
            }
        }

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
            binding.restaurantPhone.text = it.contactNumber
            binding.timing.text = "${it.openTime} - ${it.closeTime}"



//            Picasso.get().load(it.restaurantLogoUrl).into(binding.restuarantImageLogo)

        }

        val reviews = listOf(
            Review("User1", "I recently dined at Bistro Delights, and it was a fantastic experience. The ambiance is cozy and welcoming, perfect for a relaxing meal", "2024-06-27"),
            Review("User2", "I enjoyed going this place.I tried the grilled salmon, which was cooked to perfection—crispy on the outside and tender on the inside. The accompanying vegetables were fresh and flavorful. The staff was attentive and friendly, ensuring that we had everything we needed. For dessert, the chocolate lava cake was a decadent treat that I highly recommend. Overall, Bistro Delights offers delicious food, great service, and a pleasant atmosphere. I will definitely be returning!", "2024-06-26"),
            Review("User3", "Could use some improvements.", "2024-06-25")
        )

        val reviewPagerAdapter = ReviewAdapter(reviews)
        binding.reviewsViewPager.adapter = reviewPagerAdapter
        binding.springDotsIndicator.attachTo(binding.reviewsViewPager)

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