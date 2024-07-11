package com.example.khajakhoj.test

import AdsViewModel
import OnSwipeTouchListener
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageSwitcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.khajakhoj.R
import com.squareup.picasso.Picasso

class ImageActivity : AppCompatActivity() {

    private lateinit var imageSwitcher: ImageSwitcher
    private var currentIndex = 0
    private var imageUrls = listOf<String>()
    private val adsViewModel: AdsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        imageSwitcher = findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory {
            ImageView(applicationContext).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }

        adsViewModel.adsList.observe(this, Observer { urls ->
            imageUrls = urls
            if (urls.isNotEmpty()) {
                loadImage(currentIndex)
            }
        })

        imageSwitcher.setOnTouchListener(object : OnSwipeTouchListener(this@ImageActivity) {
            override fun onSwipeLeft() {
                if (currentIndex < imageUrls.size - 1) {
                    currentIndex++
                    loadImage(currentIndex)
                }
            }

            override fun onSwipeRight() {
                if (currentIndex > 0) {
                    currentIndex--
                    loadImage(currentIndex)
                }
            }
        })
    }

    private fun loadImage(index: Int) {
        Picasso.get()
            .load(imageUrls[index])
            .into(imageSwitcher.nextView as ImageView)
        imageSwitcher.showNext()
    }
}
