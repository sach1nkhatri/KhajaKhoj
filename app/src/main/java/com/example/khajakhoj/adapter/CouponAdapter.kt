import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R

class CouponAdapter(private val coupons: List<Coupon>) :
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
            holder.useCouponButton.setText("Used")
            CouponRepository.markCouponAsUsed(coupon.id)

        }
    }

    override fun getItemCount(): Int {
        return coupons.size
    }
}
