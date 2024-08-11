package com.example.khajakhoj.repository

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot

interface CouponRepository {
    fun getAllCoupons(): LiveData<DataSnapshot>
    fun redeemCoupon(couponId: String, enteredCode: String): LiveData<Result<Void?>>
    fun getUnredeemedCoupons(): LiveData<DataSnapshot>
}