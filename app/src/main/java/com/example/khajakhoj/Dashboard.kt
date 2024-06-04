package com.example.khajakhoj

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.khajakhoj.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {
    private lateinit var dashboardBinding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding.root)

        replaceFragment(HomeFragment())
        dashboardBinding.buttonNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> {
                    // Open the drawer instead of replacing fragment
                    dashboardBinding.drawerLayout.openDrawer(GravityCompat.START)
                }
                R.id.settings -> replaceFragment(SettingsFragment())
                else -> {}
            }
            true
        }

        // Set onClick listener for the slide menu button to open the drawer
        dashboardBinding.slideMenuBtn.setOnClickListener {
            dashboardBinding.drawerLayout.openDrawer(dashboardBinding.navigationView)
        }

        dashboardBinding.navigationView.setNavigationItemSelectedListener {MenuItem->
            when(MenuItem.itemId){
                R.id.coupons ->{
                    startActivity(Intent(this@Dashboard,CouponActivity::class.java))
                }
                R.id.About_us ->{
                    startActivity(Intent(this@Dashboard,AboutUsActivity::class.java))
                }
                R.id.support ->{
                    startActivity(Intent(this@Dashboard,SupportActivity::class.java))
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
