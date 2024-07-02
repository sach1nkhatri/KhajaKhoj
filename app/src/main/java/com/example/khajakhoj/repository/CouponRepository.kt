package com.example.khajakhoj.repository

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot

interface CouponRepository {
    fun getAllCoupons(): LiveData<DataSnapshot>
    fun redeemCoupon(couponId: String)
    fun getUnredeemedCoupons(): LiveData<DataSnapshot>
}