package com.example.khajakhoj.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.repository.RestaurantRepository
import com.example.khajakhoj.repository.RestaurantRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val repository: RestaurantRepository = RestaurantRepositoryImpl()

    private val _restaurantList = MutableLiveData<List<Restaurant>?>()
    val restaurantList: LiveData<List<Restaurant>?>
        get() = _restaurantList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun fetchRestaurants(cuisineType: String) {
        repository.getRestaurantsByCuisine(cuisineType) { restaurants, error ->
            if (error != null) {
                _errorMessage.postValue(error)
            } else {
                _restaurantList.postValue(restaurants)
            }
        }
    }

    private val _bookmarkResult = MutableLiveData<Pair<Boolean, String?>>()
    val bookmarkResult: LiveData<Pair<Boolean, String?>>
        get() = _bookmarkResult

    fun bookmarkRestaurant(restaurant: Restaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.bookmarkRestaurant(restaurant) { success, message ->
                _bookmarkResult.postValue(Pair(success, message))
            }
        }
    }

    private val _userBookmarks = MutableLiveData<List<Restaurant>?>()
    val userBookmarks: LiveData<List<Restaurant>?> get() = _userBookmarks

    private val _bookmarkError = MutableLiveData<String?>()
    val bookmarkError: LiveData<String?> get() = _bookmarkError

    fun getBookmarksByUserId() {
        repository.getBookmarksByUserId { restaurants, error ->
            if (error != null) {
                _bookmarkError.postValue(error)
                _userBookmarks.postValue(null) // Ensure bookmarks list is cleared if there's an error
            } else {
                _userBookmarks.postValue(restaurants)
                _bookmarkError.postValue(null) // Clear any previous error message
            }
        }
    }

    private val _unBookmarkResult = MutableLiveData<Result<Boolean>>()
    val unBookmarkResult: LiveData<Result<Boolean>> = _unBookmarkResult

    fun unBookmarkRestaurant(restaurantId: String) {
        viewModelScope.launch {
            repository.unBookmarkRestaurant(restaurantId) { success, error ->
                if (success) {
                    _unBookmarkResult.postValue(Result.success(true))
                    Log.d("RestaurantViewModel", "Unbookmarked restaurant with ID: $restaurantId")
                } else {
                    _unBookmarkResult.postValue(Result.failure(Exception(error)))
                    Log.e("RestaurantViewModel", "Failed to unbookmark restaurant: $error")
                }
            }
        }
    }

    fun isRestaurantBookmarked(restaurantId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.isRestaurantBookmarked(restaurantId) { isBookmarked ->
                callback(isBookmarked)
            }
        }
    }
}
