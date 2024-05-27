package com.example.khajakhoj

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RestaurantView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.restaurantView)
        val adapter = MyAdapter(getRestaurantList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getRestaurantList(): ArrayList<RestaurantData> {
        val restaurantList = ArrayList<RestaurantData>()
        restaurantList.add(RestaurantData(R.drawable.chiyahub, "Chiya Hub", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.kk_logo, "Cafe", "Nepali"))
        restaurantList.add(RestaurantData(R.drawable.burgerico, "Resort", "Chinese"))
        restaurantList.add(RestaurantData(R.drawable.pizzaico, "Hotel", "Thai"))
        restaurantList.add(RestaurantData(R.drawable.nepali, "Motel", "Korean"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))
        restaurantList.add(RestaurantData(R.drawable.indian, "Club", "Multi Cuisine"))

        // Add more restaurants as needed
        return restaurantList
    }
}
