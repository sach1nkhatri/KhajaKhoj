package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.model.MenuItem
import com.example.khajakhoj.repository.MenuRepository
import com.example.khajakhoj.repository.MenuRepositoryImpl

class MenuViewModel() : ViewModel() {

    private val menuRepository: MenuRepository = MenuRepositoryImpl()
    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> get() = _menuItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchMenuItems(restaurantId: String) {
        menuRepository.fetchMenuItems(restaurantId, { items ->
            _menuItems.value = items
        }, { errorMessage ->
            _error.value = errorMessage
        })
    }
}
