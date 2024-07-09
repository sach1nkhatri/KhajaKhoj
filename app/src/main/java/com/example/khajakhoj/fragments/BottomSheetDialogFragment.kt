package com.example.khajakhoj.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khajakhoj.adapter.ReviewAdapter
import com.example.khajakhoj.databinding.ReviewBottomSheetBinding
import com.example.khajakhoj.viewmodel.ReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogFragment(val restaurantId:String) : BottomSheetDialogFragment() {

    private val reviewViewModel: ReviewViewModel by activityViewModels()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var binding: ReviewBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReviewBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
        reviewAdapter = ReviewAdapter(emptyList(),false)
        binding.reviewsRecyclerView.adapter = reviewAdapter

        // Observe ViewModel LiveData
        reviewViewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            reviewAdapter.updateReviews(reviews)
        })

        // Fetch reviews for the specific restaurant (replace "restaurantId" with actual ID)
        reviewViewModel.getReviews(restaurantId)
    }
}
