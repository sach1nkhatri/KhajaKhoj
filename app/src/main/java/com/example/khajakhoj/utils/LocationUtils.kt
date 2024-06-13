package com.example.khajakhoj.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class LocationUtils(private val context: Context) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLocation(callback: (String?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        getAddressFromLocation(it.latitude, it.longitude, callback)
                    } ?: run {
                        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                        callback(null)
                    }
                }
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double, callback: (String?) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                val addressText = address.getAddressLine(0)
                Log.d("Address", "Address: $addressText")
                callback(addressText)
            } else {
                Log.d("Address", "No address found for the location.")
                callback(null)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Address", "Geocoder exception: ${e.message}")
            callback(null)
        }
    }
}
