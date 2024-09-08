package com.example.khajakhoj.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.model.Review

class ReviewAdapter(private var reviews: List<Review>, private val useAllReviewsLayout: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PAGER = 0
        private const val VIEW_TYPE_RECYCLER = 1
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingBar: RatingBar = itemView.findViewById(R.id.smallDynamicRating)
        val userTextView: TextView = itemView.findViewById(R.id.reviewUserTextView)
        val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (useAllReviewsLayout) VIEW_TYPE_RECYCLER else VIEW_TYPE_PAGER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = when (viewType) {
            VIEW_TYPE_RECYCLER -> LayoutInflater.from(parent.context).inflate(R.layout.sample_review_item, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.sample_review_list_item, parent, false)
        }
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = reviews[position]
        if (holder is ReviewViewHolder) {
            holder.ratingBar.rating = review.rating.toFloat()
            holder.userTextView.text = review.username
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
    }

    override fun getItemCount(): Int = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
