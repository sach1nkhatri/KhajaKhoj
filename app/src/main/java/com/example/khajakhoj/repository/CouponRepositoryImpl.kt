package com.example.khajakhoj.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.khajakhoj.model.Coupon
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

    override fun redeemCoupon(couponId: String, enteredCode: String): LiveData<Result<Void?>> {
        val result = MutableLiveData<Result<Void?>>()
        database.child(couponId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coupon = snapshot.getValue(Coupon::class.java)
                if (coupon != null) {
                    if (coupon.couponKey == enteredCode) {
                        database.child(couponId).child("redeemedBy").child(userId).setValue(true)
                            .addOnSuccessListener {
                                result.value = Result.success(null)
                            }
                            .addOnFailureListener { exception ->
                                result.value = Result.failure(exception)
                            }
                    } else {
                        result.value = Result.failure(Exception("Invalid code"))
                    }
                } else {
                    result.value = Result.failure(Exception("Coupon not found"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                result.value = Result.failure(error.toException())
            }
        })
        return result
    }

    override fun getUnredeemedCoupons(): LiveData<DataSnapshot> {
        val liveData = MutableLiveData<DataSnapshot>()
        try {
            database.orderByChild("redeemedBy/$userId").equalTo(null)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        liveData.value = snapshot
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _errorLiveData.value = error.message
                    }
                })
        } catch (e: Exception) {
            Log.d("Get Unredeemed coupons", e.message.toString())
        }
        return liveData
    }
}
