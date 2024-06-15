package com.example.khajakhoj.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khajakhoj.CouponAdapter
import com.example.khajakhoj.databinding.ActivityCouponsBinding

class CouponActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCouponsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCouponsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = binding.cuponRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val coupons = CouponRepository.getAvailableCoupons().toMutableList()
        val adapter = CouponAdapter(coupons)
        recyclerView.adapter = adapter

    }
}