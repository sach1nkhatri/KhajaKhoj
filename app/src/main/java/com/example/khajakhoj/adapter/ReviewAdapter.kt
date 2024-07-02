package com.example.khajakhoj.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.model.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView = itemView.findViewById(R.id.reviewUserTextView)
        val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sample_review_item, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.userTextView.text = review.userId
        holder.reviewTextView.text = review.reviewText
        holder.reviewTextView.setOnClickListener(object : View.OnClickListener {
            var isExpanded: Boolean = false

            override fun onClick(v: View) {
                val description = v as TextView
                isExpanded = !isExpanded

                if (isExpanded) {
                    description.maxLines = Int.MAX_VALUE
                    description.ellipsize = null
                } else {
                    description.maxLines = 3
                    description.ellipsize = TextUtils.TruncateAt.END
                }


            }
        })
    }

    override fun getItemCount(): Int = reviews.size
}