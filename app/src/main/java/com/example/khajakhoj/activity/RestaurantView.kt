package com.example.khajakhoj.activity

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantView : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_view)

        recyclerView = findViewById(R.id.restaurantView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(emptyList())
        recyclerView.adapter = restaurantAdapter

        database = FirebaseDatabase.getInstance().reference.child("restaurants")

        val cuisineType = intent.getStringExtra("CUISINE_TYPE")

        cuisineType?.let { fetchRestaurantFromFirebase(it) }
    }

    private fun fetchRestaurantFromFirebase(cuisineType: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val restaurantList = mutableListOf<Restaurant>()
                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                        restaurant?.let {
                            if (cuisineType.isEmpty() || it.cuisineType == cuisineType) {
                                restaurantList.add(it)
                            }
                        }
                    }
                    restaurantAdapter.updateRestaurantList(restaurantList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("TAG", "Failed to fetch restaurants: ${error.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@RestaurantView,
                        "Failed to fetch restaurants: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
