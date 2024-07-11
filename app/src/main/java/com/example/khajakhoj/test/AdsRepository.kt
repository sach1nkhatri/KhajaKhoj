package com.example.khajakhoj.test

import androidx.lifecycle.LiveData

interface AdsRepository {
    fun fetchAds(): LiveData<List<String>>
}
