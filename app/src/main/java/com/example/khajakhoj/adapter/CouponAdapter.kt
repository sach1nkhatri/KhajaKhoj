package com.example.khajakhoj

import CouponRepository
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.model.Coupon

class CouponAdapter(private val coupons: MutableList<Coupon>) :
    RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    // ViewHolder class
    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val couponCodeTextView: TextView = itemView.findViewById(R.id.codeCoupon)
        val discountTextView: TextView = itemView.findViewById(R.id.discountCoupon)
        val useCouponButton: Button = itemView.findViewById(R.id.buttonCoupon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coupon_layout, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]

        holder.couponCodeTextView.text = coupon.code
        holder.discountTextView.text = "for ${coupon.discount}% off at ${coupon.restaurant}"

        holder.useCouponButton.setOnClickListener {
            holder.useCouponButton.text = "Used"
            CouponRepository.markCouponAsUsed(coupon.id)
            // Remove the used coupon from the list and notify the adapter
            coupons.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, coupons.size)
        }
    }

    override fun getItemCount(): Int {
        return coupons.size
    }
}
