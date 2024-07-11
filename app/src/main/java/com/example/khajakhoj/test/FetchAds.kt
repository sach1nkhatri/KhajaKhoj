package com.example.khajakhoj.test

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FetchAds {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("ads")

    fun fetchAds(context: Context, recyclerView: RecyclerView) {
        storageReference.listAll().addOnSuccessListener { listResult ->
            val items = listResult.items
            if (items.isNotEmpty()) {
                val adsList = mutableListOf<String>()
                items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        adsList.add(uri.toString())
                        if (adsList.size == items.size) {
                            // When all URLs are fetched, set the adapter
                            recyclerView.adapter = AdsAdapter(context, adsList)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch image URL", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "No ads available", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to list ads", Toast.LENGTH_SHORT).show()
        }
    }
}
