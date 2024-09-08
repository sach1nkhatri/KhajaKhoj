package com.example.khajakhoj.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.RestaurantAdapter
import com.example.khajakhoj.model.Restaurant
import com.example.khajakhoj.utils.SearchManager
import com.example.khajakhoj.viewmodel.RestaurantViewModel

class RestaurantView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_view)

        recyclerView = findViewById(R.id.restaurantView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(emptyList())
        recyclerView.adapter = restaurantAdapter
        progressBar = findViewById(R.id.progressBar)


        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        val cuisineType = intent.getStringExtra("CUISINE_TYPE")
        cuisineType?.let { fetchRestaurants(it) }

        val searchView = findViewById<SearchView>(R.id.cuisineSearch)
        SearchManager.setupSearchView(searchView) { query ->
            filterRestaurants(query)
        }
    }

    private fun fetchRestaurants(cuisineType: String) {
        progressBar.visibility = View.VISIBLE
        viewModel.fetchRestaurants(cuisineType)
        viewModel.restaurantList.observe(this, Observer { restaurants ->
            progressBar.visibility = View.GONE
            restaurants?.let {
                restaurantAdapter.updateRestaurantList(it)
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            progressBar.visibility = View.GONE
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun filterRestaurants(query: String?) {
        val searchQuery = query?.lowercase() ?: ""
        val filteredList = viewModel.restaurantList.value?.filter { restaurant ->
            restaurant.name.lowercase().contains(searchQuery) ||
                    restaurant.cuisineType.lowercase().contains(searchQuery) ||
                    restaurant.address.lowercase().contains(searchQuery)
        }
        restaurantAdapter.updateRestaurantList(filteredList ?: emptyList())
    }
}
