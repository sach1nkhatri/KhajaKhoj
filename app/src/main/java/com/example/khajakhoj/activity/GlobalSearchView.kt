package com.example.khajakhoj.activity

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.RestaurantAdapter
import com.example.khajakhoj.model.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GlobalSearchView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private val restaurantList: MutableList<Restaurant> = mutableListOf()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_global_search_view)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.GlobalrestaurantView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(restaurantList)
        recyclerView.adapter = restaurantAdapter

        // Initialize SearchView
        searchView = findViewById(R.id.GlobalSearch)

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRestaurants(newText ?: "")
                return true
            }
        })

        // Fetch data from Firebase
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val database = FirebaseDatabase.getInstance().getReference("restaurants")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                restaurantList.clear()
                for (dataSnapshot in snapshot.children) {
                    val restaurant = dataSnapshot.getValue(Restaurant::class.java)
                    if (restaurant != null) {
                        restaurantList.add(restaurant)
                    }
                }
                restaurantAdapter.updateRestaurantList(restaurantList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GlobalSearchView, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterRestaurants(query: String) {
        val filteredList = restaurantList.filter { restaurant ->
            restaurant.name.contains(query, ignoreCase = true) ||
                    restaurant.cuisineType.contains(query, ignoreCase = true) ||
                    restaurant.address.contains(query, ignoreCase = true)
        }
        restaurantAdapter.updateRestaurantList(filteredList)
    }
}
