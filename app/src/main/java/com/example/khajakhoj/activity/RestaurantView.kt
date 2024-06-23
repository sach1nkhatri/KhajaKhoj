package com.example.khajakhoj.activity

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
}
