package com.example.khajakhoj.model

import android.os.Parcel
import android.os.Parcelable

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
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        readStringBooleanMap(parcel),
        readStringBooleanMap(parcel),
        readStringDoubleMap(parcel),
        readStringBooleanMap(parcel),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(fullName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(profilePictureUrl)
        writeStringBooleanMap(parcel, bookmarkedRestaurants)
        writeStringBooleanMap(parcel, reviews)
        writeStringDoubleMap(parcel, ratings)
        writeStringBooleanMap(parcel, claimedCoupons)
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

        private fun readStringBooleanMap(parcel: Parcel): Map<String, Boolean> {
            val size = parcel.readInt()
            val map = mutableMapOf<String, Boolean>()
            for (i in 0 until size) {
                val key = parcel.readString() ?: ""
                val value = parcel.readByte() != 0.toByte()
                map[key] = value
            }
            return map
        }

        private fun writeStringBooleanMap(parcel: Parcel, map: Map<String, Boolean>) {
            parcel.writeInt(map.size)
            for ((key, value) in map) {
                parcel.writeString(key)
                parcel.writeByte(if (value) 1 else 0)
            }
        }

        private fun readStringDoubleMap(parcel: Parcel): Map<String, Double> {
            val size = parcel.readInt()
            val map = mutableMapOf<String, Double>()
            for (i in 0 until size) {
                val key = parcel.readString() ?: ""
                val value = parcel.readDouble()
                map[key] = value
            }
            return map
        }

        private fun writeStringDoubleMap(parcel: Parcel, map: Map<String, Double>) {
            parcel.writeInt(map.size)
            for ((key, value) in map) {
                parcel.writeString(key)
                parcel.writeDouble(value)
            }
        }
    }
}
