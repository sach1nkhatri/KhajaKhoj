package com.example.khajakhoj.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.repository.CouponRepository
import com.google.firebase.database.DataSnapshot

class CouponViewModel(private val couponRepository: CouponRepository): ViewModel() {
    fun getAllCoupons(): LiveData<DataSnapshot> {
        return couponRepository.getAllCoupons()
    }

    fun getUnredeemedCoupons(): LiveData<DataSnapshot> {
        return couponRepository.getUnredeemedCoupons()
    }

    fun redeemCoupon(currentUserUid:String,couponId: String) {
        couponRepository.redeemCoupon(couponId)
    }
}