package com.example.khajakhoj.model

import android.os.Parcel
import android.os.Parcelable

@Suppress("UNREACHABLE_CODE")
data class User(
    val uid: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val bookmarkedRestaurants: Map<String, Boolean> = emptyMap(),
    val reviews: Map<String, Boolean> = emptyMap(),
    val ratings: Map<String, Double> = emptyMap(),
    val claimedCoupons: Map<String, Boolean> = emptyMap(),
    val createdAt: Long
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        TODO("bookmarkedRestaurants"),
        TODO("reviews"),
        TODO("ratings"),
        TODO("claimedCoupons"),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(fullName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(profilePictureUrl)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
