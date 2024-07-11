package com.example.khajakhoj.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdsRepositoryImpl : AdsRepository {

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
}
