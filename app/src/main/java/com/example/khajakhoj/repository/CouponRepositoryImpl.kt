package com.example.khajakhoj.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CouponRepositoryImpl(private val userId: String): CouponRepository {
    private val database = FirebaseDatabase.getInstance().reference.child("coupons")
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    override fun getAllCoupons(): LiveData<DataSnapshot> {
        val liveData = MutableLiveData<DataSnapshot>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                liveData.value = snapshot
            }
            override fun onCancelled(error: DatabaseError) {
                _errorLiveData.value = error.message
            }
        })
        return liveData
    }

    override fun redeemCoupon(couponId: String) {
        database.child(couponId).child("redeemedBy").child(userId).setValue(true)
    }

    override fun getUnredeemedCoupons(): LiveData<DataSnapshot> {
        val liveData = MutableLiveData<DataSnapshot>()
        database.orderByChild("redeemedBy/$userId").equalTo(null)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    liveData.value = snapshot
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorLiveData.value = error.message
                }
            })
        return liveData
    }

}