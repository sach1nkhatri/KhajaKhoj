package com.example.khajakhoj.repository

import com.example.khajakhoj.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantRepositoryImpl : RestaurantRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var restaurantsRef: DatabaseReference = database.getReference("restaurants")
    var bookmarksRef: DatabaseReference = database.getReference("bookmarks")


    override fun getRestaurantsByCuisine(
        cuisineType: String,
        callback: (List<Restaurant>?, String?) -> Unit
    ) {
        restaurantsRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

    override fun bookmarkRestaurant(restaurant: Restaurant, callback: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not authenticated")
            return
        }

        val bookmarkId = bookmarksRef.push().key
        if (bookmarkId == null) {
            callback(false, "Error generating bookmark ID")
            return
        }

        val restaurantWithUserId = mutableMapOf<String, Any?>(
            "id" to bookmarkId,
            "name" to restaurant.name,
            "address" to restaurant.address,
            "cuisineType" to restaurant.cuisineType,
            "openTime" to restaurant.openTime,
            "closeTime" to restaurant.closeTime,
            "contactNumber" to restaurant.contactNumber,
            "bikeParking" to restaurant.bikeParking,
            "carParking" to restaurant.carParking,
            "wifi" to restaurant.wifi,
            "rating" to restaurant.rating,
            "location" to restaurant.location,
            "restaurantLogoUrl" to restaurant.restaurantLogoUrl,
            "userId" to currentUser.uid
        )

        bookmarksRef.child(bookmarkId).setValue(restaurantWithUserId)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }

    override fun getBookmarksByUserId(
        callback: (List<Restaurant>?, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(null, "User not authenticated")
            return
        }
        bookmarksRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val restaurants = mutableListOf<Restaurant>()
                    for (snapshot in snapshot.children) {
                        val restaurant = snapshot.getValue(Restaurant::class.java)
                        restaurant?.let {
                            restaurants.add(it)
                        }
                    }
                    callback(restaurants, null)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, error.message)
                }
            })
    }
}
