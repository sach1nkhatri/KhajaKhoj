package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRepositoryImpl : ReviewRepository {

    private val database = FirebaseDatabase.getInstance()

    override fun submitReview(
        review: Review,
        onSuccess: () -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewsRef = database.getReference("reviews").push()
        review.reviewId = reviewsRef.key ?: return
        reviewsRef.setValue(review)
            .addOnSuccessListener { onSuccess }
            .addOnFailureListener { exception -> onFailure(DatabaseError.fromException(exception)) }
    }

    override fun getReviews(
        restaurantId: String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewRef =
            database.getReference("reviews").orderByChild("restaurantId").equalTo(restaurantId)
        reviewRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                onSuccess(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    override fun checkUserReviewExists(
        restaurantId: String,
        userId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewsRef = database.getReference("reviews").orderByChild("restaurantId_userId")
            .equalTo("${restaurantId}_$userId")
        reviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onSuccess(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    override fun calculateAverageRating(
        restaurantId: String,
        onSuccess: (Double) -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewRef =
            database.getReference("reviews").orderByChild("restaurantId").equalTo(restaurantId)
        reviewRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                val averageRating = if (reviews.isNotEmpty()) {
                    reviews.map { it.rating }.average()
                } else {
                    0.0
                }
                onSuccess(averageRating)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }
}