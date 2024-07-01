package com.example.khajakhoj

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.adapter.ReviewAdapter
import com.example.khajakhoj.model.Review
import com.example.khajakhoj.viewmodel.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class ReviewsActivity : AppCompatActivity() {

    private val reviewViewModel: ReviewViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var restaurantId: String

    private lateinit var submitReviewButton: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var averageRatingTextView: TextView
    private lateinit var reviewsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reviews)

        auth = FirebaseAuth.getInstance()
        restaurantId = "234567890345" // Replace with actual restaurantId

        // Initialize UI components
        submitReviewButton = findViewById(R.id.submitReviewButton)
        ratingBar = findViewById(R.id.ratingBar)
        reviewEditText = findViewById(R.id.reviewEditText)
        averageRatingTextView = findViewById(R.id.averageRatingTextView)
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)

        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)

        submitReviewButton.setOnClickListener {
            val rating = ratingBar.rating.toDouble()
            val reviewText = reviewEditText.text.toString()
//            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val userId = UUID.randomUUID().toString()

            val review = Review(
                restaurantId = restaurantId,
                userId = userId,
                rating = rating,
                reviewText = reviewText,
                timestamp = Date().time
            )

            reviewViewModel.submitReview(review)
        }

        reviewViewModel.reviewSubmissionStatus.observe(this, Observer { isSuccess ->
            if (isSuccess == true) {
                // Handle successful submission
                Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                reviewViewModel.getReviews(restaurantId) // Refresh reviews
                reviewViewModel.calculateAverageRating(restaurantId) // Refresh average rating
            } else {
                // Handle submission failure
                Toast.makeText(this, "Failed to submit review. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })

        reviewViewModel.reviews.observe(this, Observer { reviews ->
            // Update UI with reviews
            reviewsRecyclerView.adapter = ReviewAdapter(reviews)        })

        reviewViewModel.averageRating.observe(this, Observer { averageRating ->
            averageRatingTextView.text = "Average Rating: $averageRating"
        })

        reviewViewModel.error.observe(this, Observer { error ->
            // Handle errors
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        reviewViewModel.getReviews(restaurantId)
        reviewViewModel.calculateAverageRating(restaurantId)
    }
}
