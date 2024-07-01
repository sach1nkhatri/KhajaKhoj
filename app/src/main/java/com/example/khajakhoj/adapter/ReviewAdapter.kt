package com.example.khajakhoj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.model.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ratingBar: RatingBar = view.findViewById(R.id.reviewRatingBar)
        val reviewTextView: TextView = view.findViewById(R.id.reviewTextView)
        val userTextView: TextView = view.findViewById(R.id.userTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.ratingBar.rating = review.rating.toFloat()
        holder.reviewTextView.text = review.reviewText
        holder.userTextView.text = "User: ${review.userId}" // Replace with actual user name if available
    }

    override fun getItemCount(): Int = reviews.size
}
