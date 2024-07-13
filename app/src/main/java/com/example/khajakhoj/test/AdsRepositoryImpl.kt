package com.example.khajakhoj.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdsRepositoryImpl : AdsRepository {

    private val imageReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("restaurant_images")
    private val _restaurantImageUrls = MutableLiveData<List<String>>()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("ads")
    private val _adsList = MutableLiveData<List<String>>()

    override fun fetchAds(): LiveData<List<String>> {
        storageReference.listAll().addOnSuccessListener { listResult ->
            val items = listResult.items
            if (items.isNotEmpty()) {
                val urls = mutableListOf<String>()
                items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        urls.add(uri.toString())
                        if (urls.size == items.size) {
                            _adsList.value = urls
                        }
                    }
                }
            } else {
                _adsList.value = emptyList()
            }
        }
        return _adsList
    }

    override fun fetchRestaurantImagesByRestaurantId(restaurantId: String): LiveData<List<String>> {
        imageReference.child(restaurantId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageUrls = mutableListOf<String>()
                snapshot.children.forEach {
                    it.getValue(String::class.java)?.let { url ->
                        imageUrls.add(url)
                    }
                }
                _restaurantImageUrls.value = imageUrls
            }

            override fun onCancelled(error: DatabaseError) {
                _restaurantImageUrls.value = emptyList()
            }
        })
        return _restaurantImageUrls
    }
}
