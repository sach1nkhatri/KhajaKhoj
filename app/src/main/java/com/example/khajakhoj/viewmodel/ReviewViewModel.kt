package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.model.Review
import com.example.khajakhoj.repository.ReviewRepositoryImpl
import com.google.firebase.database.DatabaseError

class ReviewViewModel : ViewModel() {

    private val repository: ReviewRepositoryImpl = ReviewRepositoryImpl()

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val _reviewSubmissionStatus = MutableLiveData<Boolean>()
    val reviewSubmissionStatus: LiveData<Boolean> = _reviewSubmissionStatus

    private val _userReviewExists = MutableLiveData<Boolean>()
    val userReviewExists: LiveData<Boolean> = _userReviewExists

    private val _averageRating = MutableLiveData<Double>()
    val averageRating: LiveData<Double> = _averageRating

    private val _error = MutableLiveData<DatabaseError>()
    val error: LiveData<DatabaseError> = _error

    fun submitReview(review: Review) {
        repository.submitReview(review,
            onSuccess = {
                _reviewSubmissionStatus.value = true
            },
            onFailure = {
                _reviewSubmissionStatus.value = false
                _error.value = it
            })
    }

    fun getReviews(restaurantId: String) {
        repository.getReviews(restaurantId,
            onSuccess = {
                _reviews.value = it
            },
            onFailure = {
                _error.value = it
            })
    }

    fun checkUserReviewExists(restaurantId: String, userId: String) {
        repository.checkUserReviewExists(restaurantId, userId,
            onSuccess = {
                _userReviewExists.value = it
            },
            onFailure = {
                _error.value = it
            })
    }

    fun calculateAverageRating(restaurantId: String) {
        repository.calculateAverageRating(restaurantId,
            onSuccess = {
                _averageRating.value = it
            },
            onFailure = {
                _error.value = it
            })
    }
}
