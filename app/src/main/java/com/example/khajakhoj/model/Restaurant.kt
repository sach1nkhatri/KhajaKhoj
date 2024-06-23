package com.example.khajakhoj.model

import android.os.Parcel
import android.os.Parcelable

data class Restaurant(
    var id: String = "",
    val name: String = "",
    val address: String = "",  // admin input address
    val cuisineType: String = "",  // Single cuisine Type
    val openTime: String = "",
    val closeTime: String = "",
    val contactNumber: String = "",
    val bikeParking: Boolean = false,
    val carParking: Boolean = false,
    var wifi: Boolean = false,
    val rating: Double = 0.0,
    val location: String = "",  // location of restaurnat for navigantion purpose
    val restaurantLogoUrl: String = ""
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(cuisineType)
        parcel.writeString(openTime)
        parcel.writeString(closeTime)
        parcel.writeString(contactNumber)
        parcel.writeByte(if (bikeParking) 1 else 0)
        parcel.writeByte(if (carParking) 1 else 0)
        parcel.writeByte(if (wifi) 1 else 0)
        parcel.writeDouble(rating)
        parcel.writeString(location)
        parcel.writeString(restaurantLogoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurant> {
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }

}




//data class Restaurant(
//    val id: String = "",
//    val name: String = "",
//    val address: String = "",
////    val cuisineTypes: List<String> = emptyList(),
//    val cuisineTypes: String = "",
//    val openTime: String = "",
//    val closeTime: String = "",
//    val contactNumber: String = "",
//    val bikeParking: Boolean = false,
//    val carParking: Boolean = false,
//    var wifi: Boolean = false,
//
//    val rating: Int = 0,
//    val reviews: Map<String, Boolean> = emptyMap(),
//    val menuItems: Map<String, Boolean> = emptyMap(),
//    val coupons: Map<String, Boolean> = emptyMap(),
//    val location: Location = Location()
//)
