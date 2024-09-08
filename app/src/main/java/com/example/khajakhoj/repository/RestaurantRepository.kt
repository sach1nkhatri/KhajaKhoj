package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Restaurant

interface RestaurantRepository {
    fun getRestaurantsByCuisine(cuisineType: String, callback: (List<Restaurant>?, String?) -> Unit)
    fun bookmarkRestaurant(restaurant: Restaurant, callback: (Boolean, String?) -> Unit)
    fun getBookmarksByUserId(callback: (List<Restaurant>?, String?) -> Unit)
    fun unBookmarkRestaurant(restaurantId: String, callback: (Boolean, String?) -> Unit)
    fun isRestaurantBookmarked(restaurantId: String, callback: (Boolean) -> Unit)
    fun updateRestaurantRating(restaurantId: String, newRating: Double, callback: (Boolean, String?) -> Unit)

    }
