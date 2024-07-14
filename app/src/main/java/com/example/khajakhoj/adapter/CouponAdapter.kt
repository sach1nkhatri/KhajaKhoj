import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.model.Coupon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CouponAdapter(
    private val coupons: MutableList<Coupon>,
    private val currentUserUid: String,
    private val redeemCouponCallback: (String) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val couponCodeTextView: TextView = itemView.findViewById(R.id.codeCoupon)
        val discountTextView: TextView = itemView.findViewById(R.id.discountCoupon)
        val addressTextView: TextView = itemView.findViewById(R.id.addressOfRestaurant)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDate)
        val useCouponButton: Button = itemView.findViewById(R.id.buttonCoupon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.coupon_layout, parent, false)
        return CouponViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]

        holder.couponCodeTextView.text = coupon.code
        holder.discountTextView.text =
            "Get ${coupon.discountPercentage}% off on order above Rs.${coupon.minimumOrderPrice}"
        holder.addressTextView.text = "at ${coupon.restaurantName}, ${coupon.location}"
        holder.expiryDate.text = "Valid until ${coupon.validTo}"

        val isRedeemedByCurrentUser = coupon.redeemedBy.containsKey(currentUserUid)
        if (isRedeemedByCurrentUser) {
            holder.useCouponButton.text = "Used"
            holder.useCouponButton.isEnabled = false
        } else {
            holder.useCouponButton.text = "Use Coupon"
            holder.useCouponButton.isEnabled = true
            holder.useCouponButton.setOnClickListener {
                showConfirmationDialog(holder.itemView.context, coupon.id) // Show confirmation dialog
            }
        }
    }

    override fun getItemCount(): Int {
        return coupons.size
    }

    fun updateCoupons(updatedCoupons: List<Coupon>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val filteredCoupons = updatedCoupons.filter { coupon ->
            coupon.validTo >= currentDate
        }

        coupons.clear()
        coupons.addAll(filteredCoupons)
        notifyDataSetChanged()
    }

    private fun showConfirmationDialog(context: Context, couponId: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.coupon_confirm_dialog, null)
        val uniqueCodeEditText: EditText = dialogView.findViewById(R.id.uniqueCodeEditText)
        val confirmButton: Button = dialogView.findViewById(R.id.confirmButton)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        dialog.window?.setBackgroundDrawable(backgroundDrawable)

        confirmButton.setOnClickListener {
            val enteredCode = uniqueCodeEditText.text.toString()
            if (enteredCode == "UNIQUE_CODE") {
                redeemCouponCallback(couponId)
                dialog.dismiss()
                Toast.makeText(context, "Coupon used successfully.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Invalid code", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
