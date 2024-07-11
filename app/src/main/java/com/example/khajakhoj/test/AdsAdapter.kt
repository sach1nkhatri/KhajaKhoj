package com.example.khajakhoj.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.squareup.picasso.Picasso

class AdsAdapter(private val context: Context, private val adsList: List<String>) :
    RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ad, parent, false)
        return AdsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val adUrl = adsList[position]
        Picasso.get()
            .load(adUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = adsList.size

    class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
