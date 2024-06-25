package com.example.khajakhoj.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.adapter.RestaurantAdapter
import com.example.khajakhoj.viewmodel.RestaurantViewModel

class FavouritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmarks)

        recyclerView = findViewById(R.id.restaurantView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(emptyList())
        recyclerView.adapter = restaurantAdapter

        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        viewModel.getBookmarksByUserId()
        viewModel.userBookmarks.observe(this, Observer { restaurants ->
            restaurants?.let {
                restaurantAdapter.updateRestaurantList(it)
            }
        })

        viewModel.bookmarkError.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Failed to retrieve bookmarks: $it", Toast.LENGTH_SHORT).show()
            }
        })
    }
}