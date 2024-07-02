package com.example.khajakhoj.viewmodel

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

    private val _bookmarkResult = MutableLiveData<Pair<Boolean, String?>>()
    val bookmarkResult: LiveData<Pair<Boolean, String?>>
        get() = _bookmarkResult

    fun fetchRestaurants(cuisineType: String) {
        repository.getRestaurantsByCuisine(cuisineType) { restaurants, error ->
            if (error != null) {
                _errorMessage.postValue(error)
            } else {
                _restaurantList.postValue(restaurants)
            }
        }
    }

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
                _bookmarkError.value = error
                _userBookmarks.value = null // Ensure bookmarks list is cleared if there's an error
            } else {
                _userBookmarks.value = restaurants
                _bookmarkError.value = null // Clear any previous error message
            }
        }
    }
}

