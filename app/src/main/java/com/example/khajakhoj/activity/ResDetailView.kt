package com.example.khajakhoj.activity

import AdsViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.MenuAdapter
import com.example.khajakhoj.adapter.ReviewAdapter
import com.example.khajakhoj.databinding.ActivityResDetailViewBinding
import com.example.khajakhoj.fragments.ReviewBottomSheetDialogFragment
import com.example.khajakhoj.model.MenuItem
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.model.Review
import com.example.khajakhoj.utils.LoadingUtil
import com.example.khajakhoj.test.ImagePagerAdapter
import com.example.khajakhoj.utils.MapUtils
import com.example.khajakhoj.utils.SearchManager
import com.example.khajakhoj.utils.VulgarityUtils
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
    private val filteredMenuItems = mutableListOf<MenuItem>()
    private lateinit var restaurantId: String

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


        // Initialize RecyclerView for menu items
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        menuAdapter = MenuAdapter(filteredMenuItems)
        recyclerView.adapter = menuAdapter

        VulgarityUtils.initialize(this)

        menuViewModel.menuItems.observe(this) { items ->
            menuViewModel.menuItems.observe(this) { items ->
                menuItems.clear()
                menuItems.addAll(items)
                filterMenuItems("")
            }

            menuViewModel.error.observe(this) { errorMessage ->
                Toast.makeText(this, "Failed to fetch data: $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }

            val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")
            if (restaurant != null) {
                restaurantId = restaurant.id
            }

            restaurant?.let {
                updateUI(it)
                updateBookmarkButton() // Update button state based on isBookmarked
                if (!isBookmarked) {
                    checkBookmarkStatus(it.id) // Check bookmark status only if not already bookmarked
                }
                sendReview(it.id, LoadingUtil(this))
                reviewViewModel.getRandomReviews(it.id)
                menuViewModel.fetchMenuItems(it.id)
                adsViewModel.fetchRestaurantImagesByRestaurantId(it.id)


            }

            reviewViewModel.randomReviews.observe(this, Observer { reviews ->
                val reviewPagerAdapter = ReviewAdapter(reviews, true)
                binding.reviewsViewPager.adapter = reviewPagerAdapter
                binding.springDotsIndicator.attachTo(binding.reviewsViewPager)
            })

            SearchManager.setupSearchView(binding.menuSearchView) { query ->
                filterMenuItems(query)
            }

            binding.bookmarkBtn.setOnClickListener {
                restaurant?.let {
                    if (isBookmarked) {
                        restaurantViewModel.unBookmarkRestaurant(it.id)
                        observeUnBookmarkResult()
                    } else {
                        restaurantViewModel.bookmarkRestaurant(it)
                        observeBookmarkResult()
                    }
                }
            }

            binding.seeAllReviews.setOnClickListener {
                val bottomSheetFragment = ReviewBottomSheetDialogFragment(restaurantId)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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
    }

    private fun sendReview(restaurantId: String,loadingUtil: LoadingUtil) {
        binding.reviewSubmitButton.setOnClickListener {
            val reviewText = binding.reviewMessageInput.text.toString().trim()
            if (reviewText.isNotEmpty()) {
                loadingUtil.showLoading()
                VulgarityUtils.checkVulgarity(reviewText) { isClean ->
                    if (isClean) {
                        showRatingDialog(restaurantId)
                        loadingUtil.dismiss()
                    } else {
                        loadingUtil.dismiss()
                        binding.reviewMessageInput.text.clear()
                        Toast.makeText(this, "Your review contains inappropriate content.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun showRatingDialog(restaurantId: String) {
        val dialogView = layoutInflater.inflate(R.layout.rating_dialog_layout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        val backgroundDrawable =
            ContextCompat.getDrawable(this, R.drawable.custom_dialog_background)
        dialog.window?.setBackgroundDrawable(backgroundDrawable)

        val ratingBar: RatingBar = dialogView.findViewById(R.id.ratingBar)
        val positiveButton: Button = dialogView.findViewById(R.id.positiveButton)
        val negativeButton: Button = dialogView.findViewById(R.id.negativeButton)

        positiveButton.setOnClickListener {
            val rating = ratingBar.rating
            if (rating > 0) {
                dialog.dismiss()
                submitReviewWithRating(restaurantId, rating)
            } else {
                Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            }
        }

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun submitReviewWithRating(restaurantId: String, rating: Float) {
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
                        rating = rating.toDouble(),
                        reviewText = reviewText,
                        timestamp = Date().time
                    )

                    reviewViewModel.submitReview(review)
                    binding.reviewMessageInput.text.clear()
                    val bottomSheetFragment = ReviewBottomSheetDialogFragment(restaurantId)
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    Toast.makeText(this, "Review submitted", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
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


    private fun startAutoSwipe() {
        handler.postDelayed(autoSwipeRunnable, 3000) // Adjust the delay as needed
    }

    private fun stopAutoSwipe() {
        handler.removeCallbacks(autoSwipeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoSwipe()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterMenuItems(query: String?) {
        val searchQuery = query?.lowercase() ?: ""
        filteredMenuItems.clear()
        if (searchQuery.isEmpty()) {
            filteredMenuItems.addAll(menuItems)
        } else {
            filteredMenuItems.addAll(menuItems.filter {
                it.name.lowercase().contains(searchQuery) || it.description.lowercase().contains(searchQuery) || it.price.toString().contains(searchQuery)
            })
        }
        menuAdapter.notifyDataSetChanged()
    }
}

