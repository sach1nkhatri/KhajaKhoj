package com.example.khajakhoj.activity

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.RestaurantAdapter
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.viewmodel.RestaurantViewModel

class RestaurantView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_view)

        recyclerView = findViewById(R.id.restaurantView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(emptyList())
        recyclerView.adapter = restaurantAdapter

        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        val cuisineType = intent.getStringExtra("CUISINE_TYPE")
        cuisineType?.let { fetchRestaurants(it) }

        setupSearchView()
    }

    private fun fetchRestaurants(cuisineType: String) {
        viewModel.fetchRestaurants(cuisineType)
        viewModel.restaurantList.observe(this, Observer { restaurants ->
            restaurants?.let {
                restaurantAdapter.updateRestaurantList(it)
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.cuisineSearch)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuery ->
                    filterRestaurants(searchQuery)
                }
                return true
            }
        })
    }

    private fun filterRestaurants(query: String) {
        val filteredList = viewModel.restaurantList.value?.filter { restaurant ->
            restaurant.name.toLowerCase().contains(query.toLowerCase()) ||
                    restaurant.cuisineType.toLowerCase().contains(query.toLowerCase()) ||
                    restaurant.address.toLowerCase().contains(query.toLowerCase())
        }
        restaurantAdapter.updateRestaurantList(filteredList ?: emptyList())
    }
}
