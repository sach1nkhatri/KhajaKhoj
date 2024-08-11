package com.example.khajakhoj.activity

import AdsViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ProgressBar
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
import kotlin.math.log

class ResDetailView : AppCompatActivity() {
    private lateinit var binding: ActivityResDetailViewBinding
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var reviewViewModel: ReviewViewModel
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImagePagerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adsViewModel: AdsViewModel
    private lateinit var progressBar: ProgressBar
    private var isBookmarked = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()
    private val filteredMenuItems = mutableListOf<MenuItem>()
    private lateinit var restaurantId: String



    private val autoSwipeRunnable = object : Runnable {
        override fun run() {
            try {
                if (::adapter.isInitialized) {
                    val nextItem = (viewPager.currentItem + 1) % adapter.itemCount
                    viewPager.setCurrentItem(nextItem, true)
                }
                handler.postDelayed(this, 5000)
            }
            catch (e:Exception){
                Log.d("Auto Swipe",e.message.toString())
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResDetailViewBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        restaurantViewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        reviewViewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        progressBar = binding.progressBar

        viewPager = binding.viewPager

        adsViewModel = ViewModelProvider(this).get(AdsViewModel::class.java)

        adsViewModel.restaurantImageUrls.observe(this, Observer { imageUrls ->
            progressBar.visibility = View.GONE
            adapter = ImagePagerAdapter(imageUrls)
            viewPager.adapter = adapter
            binding.dotsIndicator.attachTo(viewPager)

            if (imageUrls.isNotEmpty()) {
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
            menuItems.clear()
            menuItems.addAll(items)
            filterMenuItems("")
        }

        menuViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, "Failed to fetch data: $errorMessage", Toast.LENGTH_SHORT).show()
        }

        val restaurant = intent.getParcelableExtra<Restaurant>("restaurant")
        restaurant?.let {
            restaurantId = it.id
            updateUI(it)
            updateBookmarkButton()
            if (!isBookmarked) {
                checkBookmarkStatus(it.id)
            }
            sendReview(it.id, LoadingUtil(this))
            reviewViewModel.getRandomReviews(it.id)
            menuViewModel.fetchMenuItems(it.id)
            progressBar.visibility = View.VISIBLE
            adsViewModel.fetchRestaurantImagesByRestaurantId(it.id)
        }

        reviewViewModel.randomReviews.observe(this, Observer { reviews ->
            val reviewPagerAdapter = ReviewAdapter(reviews, true)
            binding.reviewsViewPager.adapter = reviewPagerAdapter
            binding.springDotsIndicator.attachTo(binding.reviewsViewPager)
        })

        SearchManager.setupSearchView(binding.menuSearchView) { query ->
            if (query != null) {
                filterMenuItems(query)
            }
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

    private fun updateUI(restaurant: Restaurant) {
        with(binding) {
            RestaurantName.text = if (restaurant.name.length > 15) {
                "${restaurant.name.substring(0, 15)}..."
            } else {
                restaurant.name
            }
            ResturantCuisineDetail.text = restaurant.cuisineType
            RestaurantAddress.text = restaurant.address

            visit.setOnClickListener {
                MapUtils.showMapsWebViewDialog(this@ResDetailView, restaurant.location)
            }

            restaurantPhone.text = restaurant.contactNumber
            timing.text = "${restaurant.openTime} - ${restaurant.closeTime}"
            twoWheelerParking.setImageResource(if (restaurant.bikeParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            fourWheelerParking.setImageResource(if (restaurant.carParking) R.drawable.availabegreenicon else R.drawable.wrongicon)
            wifi.setImageResource(if (restaurant.wifi) R.drawable.availabegreenicon else R.drawable.wrongicon)

            val restaurantLogo = restaurant.restaurantLogoUrl
            val restaurantImageView = binding.restuarantImageLogo
            progressBar2.visibility = View.VISIBLE
            Picasso.get()
                .load(restaurantLogo)
                .into(restaurantImageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        progressBar2.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        progressBar2.visibility = View.GONE
                        Toast.makeText(this@ResDetailView, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun sendReview(restaurantId: String, loadingUtil: LoadingUtil) {
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
                        Toast.makeText(this, "Your review contains inappropriate content.", Toast.LENGTH_SHORT).show()
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
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.custom_dialog_background)
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
            binding.bookmarkBtn.setImageResource(R.drawable.favourites)
        }
    }

    private fun observeBookmarkResult() {
        restaurantViewModel.bookmarkResult.observe(this, Observer { result ->
            val (success,message) = result
            if (result != null) {
                if (success) {
                    isBookmarked = true
                    updateBookmarkButton()
                    Toast.makeText(this, "Added to Favourite", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed adding to favourite", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeUnBookmarkResult() {
        restaurantViewModel.unBookmarkResult.observe(this, Observer { result ->
            if (result != null) {
                result.onSuccess {
                    isBookmarked = false
                    updateBookmarkButton()
                    Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Toast.makeText(this, "Failed to remove from favourites", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun startAutoSwipe() {
        try {
            handler.postDelayed(autoSwipeRunnable, 5000)
        } catch (e:Exception){
            Log.d("Empty Images","Restaurant images are not provided")
        }
    }

    private fun filterMenuItems(query: String) {
        filteredMenuItems.clear()
        if (query.isBlank()) {
            filteredMenuItems.addAll(menuItems)
        } else {
            val filteredList = menuItems.filter {
                it.name.contains(query, ignoreCase = true)
            }
            filteredMenuItems.addAll(filteredList)
        }
        menuAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSwipeRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoSwipe()
    }
}

