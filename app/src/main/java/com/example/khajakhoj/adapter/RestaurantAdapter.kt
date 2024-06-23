package com.example.khajakhoj.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.ResDetailView
import com.example.khajakhoj.activity.RestaurantView
import com.example.khajakhoj.model.Restaurant
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class RestaurantAdapter(private var restaurantList: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ShapeableImageView = itemView.findViewById(R.id.restuarantImage)
        val restaurantName: TextView = itemView.findViewById(R.id.RestaurantName)
        val restaurantCuisine: TextView = itemView.findViewById(R.id.ResturantCuisine)
        val restaurantDistance: TextView = itemView.findViewById(R.id.ResturantDistance)
        val restaurantTime: TextView = itemView.findViewById(R.id.ResturantTime)
        val restaurantAddress: TextView = itemView.findViewById(R.id.restaurantAddress)
        val restaurantRating: TextView = itemView.findViewById(R.id.restaurantRating)
        val restaurantMain: View = itemView.findViewById(R.id.restaurantMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_view, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]

        Picasso.get().load(restaurant.restaurantLogoUrl).into(holder.restaurantImage)
        holder.restaurantName.text = restaurant.name
        holder.restaurantCuisine.text = restaurant.cuisineType
        holder.restaurantDistance.text = "0.5 km" // Replace with actual distance calculation if needed
        holder.restaurantTime.text = "${restaurant.openTime} - ${restaurant.closeTime}"
        holder.restaurantAddress.text = restaurant.address
        holder.restaurantRating.text = restaurant.rating.toString()

        holder.restaurantMain.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ResDetailView::class.java)
            intent.putExtra("restaurant", restaurant)
            context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun updateRestaurantList(newRestaurantList: List<Restaurant>) {
        restaurantList = newRestaurantList
        notifyDataSetChanged()
    }
}


