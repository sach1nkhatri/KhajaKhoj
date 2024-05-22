package com.example.khajakhoj

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imageSwitcher: ImageSwitcher
    private val imageIds = arrayOf(R.drawable.ad1, R.drawable.ad2, R.drawable.ad3)
    private var currentIndex = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageSwitcher = view.findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory(ViewSwitcher.ViewFactory {
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView
        })

        // Initial image
        imageSwitcher.setImageResource(imageIds[currentIndex])

        // Setup handler to switch images
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                currentIndex = (currentIndex + 1) % imageIds.size
                imageSwitcher.setImageResource(imageIds[currentIndex])
                handler.postDelayed(this, 9000) // Switch image every 3 seconds
            }
        }
        handler.postDelayed(runnable, 9000) // Start the image switcher

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable) // Stop the image switcher when the fragment is destroyed
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
