package com.example.khajakhoj.repository

import com.example.khajakhoj.model.MenuItem

interface MenuRepository {
    fun fetchMenuItems(restaurantId: String, callback: (List<MenuItem>) -> Unit, onError: (String) -> Unit)
}
