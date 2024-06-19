package com.example.khajakhoj.testActivity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.khajakhoj.R
import com.example.khajakhoj.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class UserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Manually pass the user ID
        val uid = "h38R1tte5zUJ8MRkh8CyjPOYAlC2"
        getUserData(uid)
    }

    private fun getUserData(userId: String) {
        // Reference to the user's data in the database
        val userRef = database.getReference("users").child(userId)

        // Attach a listener to read the data at our users reference
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    displayUserData(it)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log the error
                Log.e("UserActivity", "Failed to read user data", databaseError.toException())
            }
        })
    }

    private fun displayUserData(user: User) {
        // Reference UI elements
        val userNameTextView: TextView = findViewById(R.id.userNameTextView)
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val userPhoneTextView: TextView = findViewById(R.id.userPhoneTextView)
        val userAddressTextView: TextView = findViewById(R.id.userAddressTextView)
        val userProfilePictureImageView: ImageView = findViewById(R.id.userProfilePictureImageView)

        // Set user data to UI elements
        userNameTextView.text = user.fullName
        userEmailTextView.text = user.email
        userPhoneTextView.text = user.phoneNumber
        userAddressTextView.text = user.address

        // Load profile picture using Picasso
        if (user.profilePictureUrl.isNotEmpty()) {
            Log.d("UserActivity", "Profile Image url found for user: ${user.profilePictureUrl}")
            Picasso.get()
                .load(user.profilePictureUrl)
                .into(userProfilePictureImageView)
        } else{
            Log.d("UserActivity", "No profile picture found for user: ${user.fullName}")
        }
    }
}
