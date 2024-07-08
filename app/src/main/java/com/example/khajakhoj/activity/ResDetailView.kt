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
import com.example.khajakhoj.utils.MapUtils
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
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        setupImageSwitcher()
        setupGestureDetection()
        setupImageSwitchHandler()

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")

        restaurant?.let {
            updateUI(it)
            updateBookmarkButton() // Update button state based on isBookmarked
            if (!isBookmarked) {
                checkBookmarkStatus(it.id) // Check bookmark status only if not already bookmarked
            }
        }

        binding.bookmarkBtn.setOnClickListener {
            restaurant?.let {
                if (isBookmarked) {
                    viewModel.unBookmarkRestaurant(it.id)
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    observeUnBookmarkResult()
                } else {
                    viewModel.bookmarkRestaurant(it)
                    Log.d("RestaurantViewModel", "Bookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Bookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Bookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Bookmarked restaurant with ID: ${it.id}")
                    observeBookmarkResult()
                }
            }
        }

    }

    private fun updateUI(restaurant: Restaurant) {
        with(binding) {
            RestaurantName.text = if (restaurant.name.length > 15) {
                "${restaurant.name.substring(0, 15)}..."
            } else {
                restaurant.name
            }
            ResturantCuisineDetail.text = restaurant.cuisineType
            RestaurantAddress.text = restaurant.address

            visit.setOnClickListener(){
                MapUtils.showMapsWebViewDialog(this@ResDetailView,"https://www.google.com/maps/place/Radisson+Hotel+Kathmandu/@27.7156814,85.3245952,15z/data=!4m9!3m8!1s0x39eb191b0080f92b:0x3d08c08a7f53eace!5m2!4m1!1i2!8m2!3d27.7199291!4d85.3212645!16s%2Fg%2F1tdjz_mp?entry=ttu")
            }

            restaurantPhone.text = restaurant.contactNumber
            timing.text = "${restaurant.openTime} - ${restaurant.closeTime}"

            twoWheelerParking.setImageResource(if (restaurant.bikeParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            fourWheelerParking.setImageResource(if (restaurant.carParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            wifi.setImageResource(if (restaurant.wifi) R.drawable.availabegreenicon else R.drawable.wrongicon)
        }
        val reviews = listOf(
            Review("", "","User1",2.0,"I recently dined at Bistro Delights, and it was a fantastic experience. The ambiance is cozy and welcoming, perfect for a relaxing meal", 20240627),
            Review("","","User2",1.3,"I enjoyed going this place.I tried the grilled salmon, which was cooked to perfection—crispy on the outside and tender on the inside. The accompanying vegetables were fresh and flavorful. The staff was attentive and friendly, ensuring that we had everything we needed. For dessert, the chocolate lava cake was a decadent treat that I highly recommend. Overall, Bistro Delights offers delicious food, great service, and a pleasant atmosphere. I will definitely be returning!", 20240626),
            Review("", "","User3",5.0,"Could use some improvements.", 20240625)
        )

        val reviewPagerAdapter = ReviewAdapter(reviews)
        binding.reviewsViewPager.adapter = reviewPagerAdapter
        binding.springDotsIndicator.attachTo(binding.reviewsViewPager)
    }

    private fun checkBookmarkStatus(restaurantId: String) {
        viewModel.isRestaurantBookmarked(restaurantId) { isBookmarked ->
            this.isBookmarked = isBookmarked
            updateBookmarkButton()
        }
    }

    private fun updateBookmarkButton() {
        if (isBookmarked) {
            binding.bookmarkBtn.setImageResource(R.drawable.redfav)
        } else {
            binding.bookmarkBtn.setImageResource(R.drawable.favourite)
        }
    }

    private fun observeBookmarkResult() {
        viewModel.bookmarkResult.observe(this, Observer { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show()
                isBookmarked = true
                updateBookmarkButton()
            } else {
                Toast.makeText(this, "Failed to add Favourites: $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeUnBookmarkResult() {
        viewModel.unBookmarkResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show()
                isBookmarked = false
                updateBookmarkButton()
            }.onFailure {
                Toast.makeText(this, "Removed from Favourites failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
