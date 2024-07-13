package com.example.khajakhoj.activity

import AdsViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.MenuAdapter
import com.example.khajakhoj.adapter.ReviewAdapter
import com.example.khajakhoj.databinding.ActivityResDetailViewBinding
import com.example.khajakhoj.model.MenuItem
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.model.Review
import com.example.khajakhoj.test.ImagePagerAdapter
import com.example.khajakhoj.utils.MapUtils
import com.example.khajakhoj.viewmodel.MenuViewModel
import com.example.khajakhoj.viewmodel.RestaurantViewModel
import com.example.khajakhoj.viewmodel.ReviewViewModel
import com.example.khajakhoj.viewmodel.UserViewModel
import com.squareup.picasso.Picasso
import java.util.Date

class ResDetailView : AppCompatActivity() {
    private lateinit var binding: ActivityResDetailViewBinding

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var reviewViewModel: ReviewViewModel
    private val userViewModel: UserViewModel by viewModels()

//    private lateinit var imageSwitcher: ImageSwitcher
//    private lateinit var gestureDetector: GestureDetector
////    private val imageIds = listOf(R.drawable.ad3, R.drawable.ad2, R.drawable.ad4)
//    private var currentIndex = 0
//    private lateinit var handler: Handler
//    private lateinit var runnable: Runnable

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImagePagerAdapter
    private val handler = Handler(Looper.getMainLooper())

    private val autoSwipeRunnable = object : Runnable {
        override fun run() {
            val nextItem = (viewPager.currentItem + 1) % adapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 5000) // Adjust the delay as needed
        }
    }
    private lateinit var adsViewModel: AdsViewModel

    private var isBookmarked = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResDetailViewBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        restaurantViewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        reviewViewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]


        viewPager = binding.viewPager

        adsViewModel = ViewModelProvider(this).get(AdsViewModel::class.java)
        adsViewModel.restaurantImageUrls.observe(this, Observer { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                adapter = ImagePagerAdapter(imageUrls)
                viewPager.adapter = adapter
                binding.dotsIndicator.attachTo(viewPager)
                startAutoSwipe()
            }
        })

//        setupImageSwitcher()
//        val adsViewModel: AdsViewModel by viewModels()
//        adsViewModel.adsList.observe(this, Observer { adsList ->
//            if (adsList.isNotEmpty()) {
//                startImageSwitcher(adsList)
//            } else {
//                Toast.makeText(this, "No ads available", Toast.LENGTH_SHORT).show()
//            }
//        })

//        setupGestureDetection()
//        setupImageSwitchHandler()

        // Initialize RecyclerView for menu items
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        menuAdapter = MenuAdapter(menuItems)
        recyclerView.adapter = menuAdapter

        menuViewModel.menuItems.observe(this) { items ->
            menuItems.clear()
            menuItems.addAll(items)
            menuAdapter.notifyDataSetChanged()
        }

        menuViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, "Failed to fetch data: $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")

        restaurant?.let {
            updateUI(it)
            updateBookmarkButton() // Update button state based on isBookmarked
            if (!isBookmarked) {
                checkBookmarkStatus(it.id) // Check bookmark status only if not already bookmarked
            }
            sendReview(it.id)
            reviewViewModel.getRandomReviews(it.id)
            menuViewModel.fetchMenuItems(it.id)
            adsViewModel.fetchRestaurantImagesByRestaurantId(it.id)


        }

        reviewViewModel.randomReviews.observe(this, Observer { reviews ->
            val reviewPagerAdapter = ReviewAdapter(reviews)
            binding.reviewsViewPager.adapter = reviewPagerAdapter
            binding.springDotsIndicator.attachTo(binding.reviewsViewPager)
        })

        binding.bookmarkBtn.setOnClickListener {
            restaurant?.let {
                if (isBookmarked) {
                    restaurantViewModel.unBookmarkRestaurant(it.id)
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: ${it.id}")
                    observeUnBookmarkResult()
                } else {
                    restaurantViewModel.bookmarkRestaurant(it)
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

            visit.setOnClickListener() {
                MapUtils.showMapsWebViewDialog(
                    this@ResDetailView,
                    "https://www.google.com/maps/place/The+Soaltee+Kathmandu/@27.6917812,85.2620819,15.01z/data=!4m9!3m8!1s0x39eb18609488cdb7:0x44edd8fc9a17af63!5m2!4m1!1i2!8m2!3d27.7005975!4d85.291006!16s%2Fg%2F1tcv4qxf?entry=ttu"
                )
            }

            restaurantPhone.text = restaurant.contactNumber
            timing.text = "${restaurant.openTime} - ${restaurant.closeTime}"

            twoWheelerParking.setImageResource(if (restaurant.bikeParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            fourWheelerParking.setImageResource(if (restaurant.carParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            wifi.setImageResource(if (restaurant.wifi) R.drawable.availabegreenicon else R.drawable.wrongicon)

        }
//        val reviews = listOf(
//            Review("", "","User1",2.0,"I recently dined at Bistro Delights, and it was a fantastic experience. The ambiance is cozy and welcoming, perfect for a relaxing meal", 20240627),
//            Review("","","User2",1.3,"I enjoyed going this place.I tried the grilled salmon, which was cooked to perfection—crispy on the outside and tender on the inside. The accompanying vegetables were fresh and flavorful. The staff was attentive and friendly, ensuring that we had everything we needed. For dessert, the chocolate lava cake was a decadent treat that I highly recommend. Overall, Bistro Delights offers delicious food, great service, and a pleasant atmosphere. I will definitely be returning!", 20240626),
//            Review("", "","User3",5.0,"Could use some improvements.", 20240625)
//        )
//
//        val reviewPagerAdapter = ReviewAdapter(reviews)
//        binding.reviewsViewPager.adapter = reviewPagerAdapter
//        binding.springDotsIndicator.attachTo(binding.reviewsViewPager)
    }

    private fun sendReview(restaurantId: String) {
        binding.reviewSubmitButton.setOnClickListener {
            val reviewRating = binding.userRestaurantRating.rating // Get the rating as Float
            val reviewText = binding.reviewMessageInput.text.toString()

            userViewModel.currentUser.observe(this) { user ->
                if (user != null) {
                    val userId = user.uid
                    val fullName = user.fullName

                    if (reviewText.isNotBlank()) {
                        val review = Review(
                            restaurantId = restaurantId,
                            userId = userId,
                            username = fullName,
                            rating = reviewRating.toDouble(), // Convert Float to Double
                            reviewText = reviewText,
                            timestamp = Date().time
                        )

                        reviewViewModel.submitReview(review)
                        binding.reviewMessageInput.text.clear() // Clear input field after submitting
                        binding.userRestaurantRating.rating = 0f // Reset the rating bar

                    } else {
                        Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun checkBookmarkStatus(restaurantId: String) {
        restaurantViewModel.isRestaurantBookmarked(restaurantId) { isBookmarked ->
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
        restaurantViewModel.bookmarkResult.observe(this, Observer { result ->
            val (success, message) = result
            if (success) {
                Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show()
                isBookmarked = true
                updateBookmarkButton()
            } else {
                Toast.makeText(this, "Failed to add Favourites: $message", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun observeUnBookmarkResult() {
        restaurantViewModel.unBookmarkResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show()
                isBookmarked = false
                updateBookmarkButton()
            }.onFailure {

                Toast.makeText(
                    this,
                    "Removed from Favourites failed: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

//    private fun setupImageSwitcher() {
//        imageSwitcher = binding.imageSwitcher
//        imageSwitcher.setFactory {
//            val imageView = ImageView(this)
//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView
//        }
//    }

//    private fun setupGestureDetection() {
//        gestureDetector = GestureDetector(this, SwipeGestureListener())
//        val cardView: CardView = binding.ResPhotos
//        cardView.setOnTouchListener { view, event ->
//            view.performClick() // Optional: to handle click events as well
//            val result = gestureDetector.onTouchEvent(event)
//            Log.d("GestureDetection", "Touch event: ${event.action}, Result: $result")
//            result
//        }
//    }

//    private fun startImageSwitcher(adsList: List<String>) {
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                Picasso.get()
//                    .load(adsList[currentIndex])
//                    .into(imageSwitcher.currentView as ImageView)
//
//                currentIndex = (currentIndex + 1) % adsList.size
//                handler.postDelayed(this, 3000) // Switch image every 3 seconds
//            }
//        }, 3000)
//    }

//    private fun setupImageSwitchHandler() {
//        handler = Handler(Looper.getMainLooper())
//        runnable = object : Runnable {
//            override fun run() {
//                currentIndex = (currentIndex + 1) % imageIds.size
//                imageSwitcher.setImageResource(imageIds[currentIndex])
//                handler.postDelayed(this, 3000) // Switch image every 3 seconds
//            }
//        }
//        handler.postDelayed(runnable, 3000) // Start the image switcher
//
//    }


//    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
//        private val SWIPE_THRESHOLD = 100
//        private val SWIPE_VELOCITY_THRESHOLD = 100
//
//        override fun onFling(
//            e1: MotionEvent?,
//            e2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            if (e1 == null) return false
//
//            val diffX = e2.x - e1.x
//            val diffY = e2.y - e1.y
//
//            return if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(
//                    velocityX
//                ) > SWIPE_VELOCITY_THRESHOLD
//            ) {
//                if (diffX > 0) {
//                    onSwipeRight()
//                } else {
//                    onSwipeLeft()
//                }
//                true
//            } else {
//                false
//            }
//        }
//    }

//    private fun onSwipeLeft() {
//        Log.d("SwipeGesture", "Swiped Left")
//        currentIndex = (currentIndex + 1) % imageIds.size
//        imageSwitcher.setImageResource(imageIds[currentIndex])
//    }
//
//    private fun onSwipeRight() {
//        Log.d("SwipeGesture", "Swiped Right")
//        currentIndex = (currentIndex - 1 + imageIds.size) % imageIds.size
//        imageSwitcher.setImageResource(imageIds[currentIndex])
//    }

    private fun startAutoSwipe() {
        handler.postDelayed(autoSwipeRunnable, 3000) // Adjust the delay as needed
    }

    private fun stopAutoSwipe() {
        handler.removeCallbacks(autoSwipeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoSwipe()
//        handler.removeCallbacks(runnable)
//        handler.removeCallbacksAndMessages(null)
    }
}