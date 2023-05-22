package com.sharehands.sharehands_frontend.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemRecommendServiceBinding
import com.sharehands.sharehands_frontend.network.home.SuggestedItem
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity

class SuggestedServiceFragment(val item: SuggestedItem): Fragment() {
    private lateinit var binding: ItemRecommendServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_recommend_service, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .load(item.thumbnailUrl)
            .into(binding.ivRecommendThumbnail)

        binding.tvRecommendTitle.text = item.title
        binding.tvRecommendStartDate.text = item.period
        binding.tvRecommendWeekday.text = item.weekday
        binding.tvStartTime.text = item.time
        binding.tvRecommendLocation.text = item.location

        binding.layoutService.setOnClickListener {
            val serviceId = item.serviceId.toInt()
            val intent = Intent(requireContext(), ServiceDetailActivity::class.java)
            intent.putExtra("serviceId", serviceId)
            startActivity(intent)
        }

    }
}