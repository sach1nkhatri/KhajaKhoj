package com.example.khajakhoj.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.ResDetailView
import com.example.khajakhoj.model.Restaurant
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class RestaurantAdapter(private var restaurantList: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(), Filterable {

    private var filteredList: List<Restaurant> = restaurantList

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ShapeableImageView = itemView.findViewById(R.id.restuarantImage)
        val restaurantName: TextView = itemView.findViewById(R.id.RestaurantName)
        val restaurantCuisine: TextView = itemView.findViewById(R.id.ResturantCuisine)
        val restaurantDistance: TextView = itemView.findViewById(R.id.ResturantDistance)
        val restaurantTime: TextView = itemView.findViewById(R.id.ResturantTime)
        val restaurantAddress: TextView = itemView.findViewById(R.id.restaurantAddress)
        val restaurantRating: TextView = itemView.findViewById(R.id.restaurantRating)
        val restaurantMain: View = itemView.findViewById(R.id.restaurantMain)
        val imageProgressBar: ProgressBar = itemView.findViewById(R.id.imageProgressBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_view, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.imageProgressBar.visibility = View.VISIBLE
        Picasso.get().load(restaurant.restaurantLogoUrl).into(holder.restaurantImage, object :
            Callback {
            override fun onSuccess() {
                holder.imageProgressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                holder.imageProgressBar.visibility = View.GONE
            }
        })
        holder.restaurantName.text = restaurant.name
        holder.restaurantCuisine.text = restaurant.cuisineType
        holder.restaurantDistance.text = "0.5 km" // Replace with actual distance calculation if needed
        holder.restaurantTime.text = "${restaurant.openTime} - ${restaurant.closeTime}"

        // Limit address to 16 characters
        if (restaurant.address.length > 16) {
            holder.restaurantAddress.text = restaurant.address.substring(0, 16) + "..."
        } else {
            holder.restaurantAddress.text = restaurant.address
        }

        holder.restaurantRating.text = restaurant.rating.toString()

        holder.restaurantMain.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ResDetailView::class.java)
            intent.putExtra("restaurant", restaurant)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateRestaurantList(newRestaurantList: List<Restaurant>) {
        restaurantList = newRestaurantList
        filteredList = newRestaurantList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.ROOT)
                val results = FilterResults()
                results.values = if (query.isNullOrEmpty()) {
                    restaurantList
                } else {
                    restaurantList.filter {
                        it.name.lowercase(Locale.ROOT).contains(query) ||
                                it.cuisineType.lowercase(Locale.ROOT).contains(query) ||
                                it.address.lowercase(Locale.ROOT).contains(query)
                    }
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Restaurant>
                notifyDataSetChanged()
            }
        }
    }
}

