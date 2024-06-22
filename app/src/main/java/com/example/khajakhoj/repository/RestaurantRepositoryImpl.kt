package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantRepositoryImpl : RestaurantRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("restaurants")

    override fun getRestaurantsByCuisine(cuisineType: String, callback: (List<Restaurant>?, String?) -> Unit) {
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
                    callback(restaurantList, null)
                } else {
                    callback(null, "No restaurants found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, "Failed to fetch restaurants: ${error.message}")
            }
        })
    }
}
