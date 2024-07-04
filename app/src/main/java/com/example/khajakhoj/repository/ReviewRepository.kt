package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Review
import com.google.firebase.database.DatabaseError

interface ReviewRepository {
    fun submitReview(
        review: Review,
        onSuccess: () -> Unit,
        onFailure: (DatabaseError) -> Unit
    )

    fun getReviews(
        restaurantId: String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (DatabaseError) -> Unit
    )

    fun checkUserReviewExists(
        restaurantId: String,
        userId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (DatabaseError) -> Unit
    )

    fun calculateAverageRating(
        restaurantId: String,
        onSuccess: (Double) -> Unit,
        onFailure: (DatabaseError) -> Unit
    )

    fun getRandomReviews(
        restaurantId: String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (DatabaseError) -> Unit
    )
}