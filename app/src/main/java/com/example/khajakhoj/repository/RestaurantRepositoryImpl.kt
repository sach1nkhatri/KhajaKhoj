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

        val bookmarkId = restaurant.id
        val userId = currentUser.uid

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
            "restaurantId" to restaurant.id,
            "userId" to currentUser.uid
        )

        bookmarksRef.child(userId).child(bookmarkId).setValue(restaurantWithUserId)
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
        bookmarksRef.child(userId)
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

//        bookmarksRef.orderByChild("userId").equalTo(userId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val restaurants = mutableListOf<Restaurant>()
//                    for (snapshot in snapshot.children) {
//                        val restaurant = snapshot.getValue(Restaurant::class.java)
//                        restaurant?.let {
//                            restaurants.add(it)
//                        }
//                    }
//                    callback(restaurants, null)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    callback(null, error.message)
//                }
//            })
    }

    override fun unBookmarkRestaurant(restaurantId: String, callback: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not authenticated")
            return
        }

        val userId = currentUser.uid

        bookmarksRef.child(userId).child(restaurantId).removeValue()
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
//        bookmarksRef.orderByChild("userId").equalTo(currentUser.uid)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val bookmarkToRemove = snapshot.children.firstOrNull {
//                        it.child("restaurantId").value == restaurantId
//                    }
//
//                    if (bookmarkToRemove != null) {
//                        bookmarkToRemove.ref.removeValue()
//                            .addOnSuccessListener {
//                                callback(true, null)
//                            }
//                            .addOnFailureListener { exception ->
//                                callback(false, exception.message)
//                            }
//                    } else {
//                        callback(false, "Restaurant not found in bookmarks")
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    callback(false, error.message)
//                }
//            })
    }

    override fun isRestaurantBookmarked(restaurantId: String, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false)
            return
        }

        val userId = currentUser.uid

        bookmarksRef.child(userId).child(restaurantId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false)
                }
            })

//        bookmarksRef.orderByChild("userId").equalTo(currentUser.uid)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val isBookmarked = snapshot.children.any {
//                        it.child("restaurantId").value == restaurantId
//                    }
//                    callback(isBookmarked)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    callback(false)
//                }
//            })
    }

    override fun updateRestaurantRating(restaurantId: String, newRating: Double, callback: (Boolean, String?) -> Unit) {
        val restaurantRef = restaurantsRef.child(restaurantId)
        restaurantRef.child("rating").setValue(newRating)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message)
            }
    }


}
