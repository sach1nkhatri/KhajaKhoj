package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Restaurant

interface RestaurantRepository {
    fun getRestaurantsByCuisine(cuisineType: String, callback: (List<Restaurant>?, String?) -> Unit)
}
