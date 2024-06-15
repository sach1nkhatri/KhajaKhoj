package com.example.khajakhoj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.RestaurantData
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val restaurantList: ArrayList<RestaurantData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = restaurantList[position]
        holder.restaurantImage.setImageResource(currentItem.titleImage)
        holder.restaurantName.text = currentItem.restaurantTitle
        holder.restaurantCuisine.text = currentItem.restaurantCuisine
        // You can set other text views here like distance, time, etc.
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ShapeableImageView = itemView.findViewById(R.id.restuarantImage)
        val restaurantName: TextView = itemView.findViewById(R.id.RestaurantName)
        val restaurantCuisine: TextView = itemView.findViewById(R.id.ResturantCuisine)
        // You can add other text views here for distance, time, etc.
    }
}
