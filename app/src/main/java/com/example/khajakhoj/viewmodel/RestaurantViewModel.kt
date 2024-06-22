package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.repository.RestaurantRepository
import com.example.khajakhoj.repository.RestaurantRepositoryImpl

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
}

