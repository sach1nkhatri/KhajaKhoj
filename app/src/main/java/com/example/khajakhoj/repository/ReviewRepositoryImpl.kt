package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRepositoryImpl : ReviewRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun submitReview(
        review: Review,
        onSuccess: () -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
//        val currentUser = auth.currentUser
        val reviewsRef = database.getReference("reviews").push()
        review.reviewId = reviewsRef.key ?: return
//        review.userId = currentUser?.uid.toString()
//        review.username = currentUser?.displayName.toString()

        reviewsRef.setValue(review)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(DatabaseError.fromException(exception)) }
    }

    override fun getReviews(
        restaurantId: String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewsQuery =
            database.getReference("reviews").orderByChild("restaurantId").equalTo(restaurantId)
        reviewsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
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
        val reviewQuery = database.getReference("reviews").orderByChild("restaurantId_userId")
            .equalTo("${restaurantId}_$userId")
        reviewQuery.addListenerForSingleValueEvent(object : ValueEventListener {
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
        val reviewsQuery =
            database.getReference("reviews").orderByChild("restaurantId").equalTo(restaurantId)
        reviewsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
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


    override fun getRandomReviews(
        restaurantId: String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (DatabaseError) -> Unit
    ) {
        val reviewsQuery =
            database.getReference("reviews").orderByChild("restaurantId").equalTo(restaurantId)
        reviewsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = snapshot.children.mapNotNull { it.getValue(Review::class.java) }
                val randomReviews = reviews.shuffled().take(3)
                onSuccess(randomReviews)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }
}
