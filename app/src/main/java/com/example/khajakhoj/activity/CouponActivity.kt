package com.example.khajakhoj.activity

import CouponAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivityCouponsBinding
import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.repository.CouponRepository
import com.example.khajakhoj.repository.CouponRepositoryImpl
import com.example.khajakhoj.viewmodel.CouponViewModel
import com.google.firebase.auth.FirebaseAuth

class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponsBinding
    private lateinit var couponAdapter: CouponAdapter
    private lateinit var coupons: MutableList<Coupon>
    private lateinit var currentUserUid: String
    private lateinit var couponViewModel: CouponViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coupons = mutableListOf()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserUid = currentUser.uid
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val repo = CouponRepositoryImpl(currentUserUid)
        couponViewModel = CouponViewModel(repo)

        val recyclerView: RecyclerView = findViewById(R.id.cuponRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        couponAdapter = CouponAdapter(coupons, currentUserUid){
            couponId ->
                couponViewModel.redeemCoupon(currentUserUid,couponId)

        }
        recyclerView.adapter = couponAdapter

        couponViewModel.getUnredeemedCoupons().observe(this, Observer { dataSnapshot ->
            val coupons = mutableListOf<Coupon>()
            dataSnapshot?.let {
                for (couponSnapshot in dataSnapshot.children) {
                    val coupon = couponSnapshot.getValue(Coupon::class.java)
                    coupon?.let {
                        coupons.add(it)
                    }
                }
            }
            couponAdapter.updateCoupons(coupons)
        })

    }
}