package com.example.khajakhoj.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageSwitcher
import android.widget.ImageView
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

        setupViews()
        setupImageSwitcher()
        setupGestureDetection()
        setupImageSwitchHandler()

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")
        restaurant?.let {
            updateUI(it)
            checkBookmarkStatus(it.id)
        }

        binding.bookmarkBtn.setOnClickListener {
            restaurant?.let {
                viewModel.bookmarkRestaurant(it)
                observeBookmarkResult()
            }
        }

        binding.unBookmarkBtn.setOnClickListener {
            restaurant?.let {
                viewModel.unBookmarkRestaurant(it.id)
                observeUnBookmarkResult()
            }
        }

    }

    private fun updateUI(restaurant: Restaurant) {
        with(binding) {
            RestaurantName.text = restaurant.name
            ResturantCuisineDetail.text = restaurant.cuisineType
            RestaurantAddress.text = restaurant.address
            address.text = restaurant.address
            restaurantPhone.text = restaurant.contactNumber
            timing.text = "${restaurant.openTime} - ${restaurant.closeTime}"

            twoWheelerParking.setImageResource(if (restaurant.bikeParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            fourWheelerParking.setImageResource(if (restaurant.carParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            wifi.setImageResource(if (restaurant.wifi) R.drawable.availabegreenicon else R.drawable.wrongicon)
        }
    }

    private fun checkBookmarkStatus(restaurantId: String) {
        viewModel.isRestaurantBookmarked(restaurantId) { isBookmarked ->
            if (isBookmarked) {
                binding.bookmarkBtn.setImageResource(R.drawable.favourites)
                binding.unBookmarkBtn.isEnabled = true
                binding.bookmarkBtn.isEnabled = false
            } else {
                binding.bookmarkBtn.setImageResource(R.drawable.favourite)
                binding.bookmarkBtn.isEnabled = true
                binding.unBookmarkBtn.isEnabled = false
            }
        }
    }

    private fun observeBookmarkResult() {
        viewModel.bookmarkResult.observe(this, Observer { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(this, "Bookmark added successfully!", Toast.LENGTH_SHORT).show()
                binding.bookmarkBtn.setImageResource(R.drawable.favourites)
                binding.unBookmarkBtn.isEnabled = true
                binding.bookmarkBtn.isEnabled = false
            } else {
                Toast.makeText(this, "Failed to add bookmark: $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeUnBookmarkResult() {
        viewModel.unBookmarkResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Unbookmarked successfully", Toast.LENGTH_SHORT).show()
                binding.bookmarkBtn.setImageResource(R.drawable.favourite)
                binding.bookmarkBtn.isEnabled = true
                binding.unBookmarkBtn.isEnabled = false
            }.onFailure {
                Toast.makeText(this, "Unbookmark failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupViews() {
        // Initialize other views if needed
    }

    private fun setupImageSwitcher() {
        imageSwitcher = binding.imageSwitcher
        imageSwitcher.setFactory {
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView
        }
    }

    private fun setupGestureDetection() {
        gestureDetector = GestureDetector(this, SwipeGestureListener())
        val cardView: CardView = binding.ResPhotos
        cardView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
    }

    private fun setupImageSwitchHandler() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                currentIndex = (currentIndex + 1) % imageIds.size
                imageSwitcher.setImageResource(imageIds[currentIndex])
                handler.postDelayed(this, 5000) // Switch image every 5 seconds
            }
        }
        handler.postDelayed(runnable, 5000) // Start the image switcher
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
            if (e1 == null) return false

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
        currentIndex = (currentIndex + 1) % imageIds.size
        imageSwitcher.setImageResource(imageIds[currentIndex])
    }

    private fun onSwipeRight() {
        currentIndex = (currentIndex - 1 + imageIds.size) % imageIds.size
        imageSwitcher.setImageResource(imageIds[currentIndex])
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
