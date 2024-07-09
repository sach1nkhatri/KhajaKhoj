package com.example.khajakhoj.repository

import com.example.khajakhoj.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuRepositoryImpl : MenuRepository {

    override fun fetchMenuItems(restaurantId: String, callback: (List<MenuItem>) -> Unit, onError: (String) -> Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("menus/$restaurantId")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuItems = mutableListOf<MenuItem>()
                snapshot.children.forEach { itemSnapshot ->
                    val name = itemSnapshot.child("name").getValue(String::class.java) ?: ""
                    val description = itemSnapshot.child("description").getValue(String::class.java) ?: ""
                    val price = itemSnapshot.child("price").getValue(String::class.java)?.toInt() ?: 0
                    val menuItem = MenuItem(name, description, price)
                    menuItems.add(menuItem)
                }
                callback(menuItems)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}