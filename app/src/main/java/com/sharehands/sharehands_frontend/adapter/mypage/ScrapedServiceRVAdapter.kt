package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemServiceParticipatedBinding
import com.sharehands.sharehands_frontend.databinding.ItemServiceScrapBinding
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.mypage.ParticipatedServiceActivity
import com.sharehands.sharehands_frontend.view.mypage.ScrapedServiceActivity
import com.sharehands.sharehands_frontend.view.search.ReviewWriteActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class ScrapedServiceRVAdapter(private val context: ScrapedServiceActivity, private val viewModel: ServiceMgtViewModel,
                              private val servicesList: ArrayList<RecruitedService?>?)
    : RecyclerView.Adapter<ScrapedServiceRVAdapter.ScrapedServiceViewHolder>(){
        inner class ScrapedServiceViewHolder(private val binding: ItemServiceScrapBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: RecruitedService, viewModel: ServiceMgtViewModel) {
                    val token = SharedPreferencesManager.getInstance(context).getString("token", "null")
                    val cancelBtn = binding.ivScrapCancel
                    val serviceId = current.serviceId.toInt()

                    Glide.with(context)
                        .load(current.imageUrl)
                        .into(binding.ivServicePreviewThumbnail)

                    Glide.with(context)
                        .load(current.profileUrl)
                        .into(binding.ivServicePreviewUser)

                    binding.tvServicePreviewUser.text = current.nickName
                    binding.tvPreviewTitle.text = current.serviceName
                    val locationList = current.location.split(" ")
                    if (locationList.size >= 2) {
                        val district = locationList[1]
                        binding.tvPreviewLocation.text = district
                    }
                    binding.tvPreviewPeriod.text = current.date
                    binding.tvPreviewPeople.text = "${current.maxNum}명"
                    binding.tvPreviewDay.text = current.dow

                    cancelBtn.setOnClickListener {
                        // 스크랩 취소 api
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapedServiceRVAdapter.ScrapedServiceViewHolder {
        val binding = ItemServiceScrapBinding.inflate(LayoutInflater.from(context), parent, false)
        return ScrapedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapedServiceRVAdapter.ScrapedServiceViewHolder, position: Int) {
        servicesList!![position]?.let { holder.bind(context, it, viewModel) }
    }

    override fun getItemCount(): Int {
        return try {
            servicesList!!.size
        } catch (e: Exception) {
            Log.d("스크랩 service list failed", "${e.message}")
            0
        }

    }
}