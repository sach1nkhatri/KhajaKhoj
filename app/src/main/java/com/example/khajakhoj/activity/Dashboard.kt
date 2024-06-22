package com.example.khajakhoj.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.khajakhoj.R
import com.example.khajakhoj.databinding.ActivityDashboardBinding
import com.example.khajakhoj.fragment.ProfileFragment
import com.example.khajakhoj.utils.Utils
import com.example.khajakhoj.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Dashboard : AppCompatActivity() {
    private lateinit var dashboardBinding: ActivityDashboardBinding
    private val viewModel: UserViewModel by viewModels()
    private var logoutDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        replaceFragment(HomeFragment())
        dashboardBinding.buttonNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
                else -> {}
            }
            true
        }

        // Set onClick listener for the slide menu button to open the drawer
        dashboardBinding.slideMenuBtn.setOnClickListener {
            dashboardBinding.drawerLayout.openDrawer(dashboardBinding.navigationView)
        }

        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                Log.d("Profile Fragment", "User user: $user")
                Log.d("Profile Fragment", "email: ${user.email}")
                Log.d("Profile Fragment", "fullName: ${user.fullName}")

                val fullName = user.fullName
                val email = user.email

                updateDrawerHeader(fullName, email)


            } else {
                Log.d("UserViewModel", "User not logged in")
            }
        }

        dashboardBinding.navigationView.setNavigationItemSelectedListener {MenuItem->

            when(MenuItem.itemId){
                R.id.coupons ->{
                    startActivity(Intent(this@Dashboard, CouponActivity::class.java))
                }
                R.id.About_us ->{
                    startActivity(Intent(this@Dashboard, AboutUsActivity::class.java))
                }
                R.id.support ->{
                    startActivity(Intent(this@Dashboard, SupportActivity::class.java))
                }
                R.id.Log_Out ->{
                    Utils.logOut(this) {
                        // Callback for when logout is confirmed
                        redirectToLoginPage()
                    }.also {
                        // Store reference to the dialog
                        logoutDialog = it
                    }
                }
            }
            true
        }
        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun updateDrawerHeader(fullName: String, email: String) {
        // Update drawer header
        val headerView = dashboardBinding.navigationView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.fullNameUser)
        val emailTextView = headerView.findViewById<TextView>(R.id.emailUser)

        nameTextView.text = fullName
        emailTextView.text = email
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun redirectToLoginPage() {
        startActivity(Intent(this, LoginPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}