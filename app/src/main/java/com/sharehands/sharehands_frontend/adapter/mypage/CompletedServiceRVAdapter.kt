package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemServiceParticipatedBinding
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.mypage.ParticipatedServiceActivity
import com.sharehands.sharehands_frontend.view.search.ReviewWriteActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel

class CompletedServiceRVAdapter(private val context: ParticipatedServiceActivity, private val viewModel: ServiceMgtViewModel,
                                private val servicesList: ArrayList<RecruitedService?>?
)
    : RecyclerView.Adapter<CompletedServiceRVAdapter.CompletedServiceViewHolder>(){
        inner class CompletedServiceViewHolder(private val binding: ItemServiceParticipatedBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: RecruitedService, viewModel: ServiceMgtViewModel) {
                    val token = SharedPreferencesManager.getInstance(context).getString("token", "null")
                    val reviewBtn = binding.ivWriteReview
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

                    reviewBtn.setOnClickListener {
                        val intent = Intent(context, ReviewWriteActivity::class.java)
                        (context as ParticipatedServiceActivity).finish()
                        context.startActivity(intent)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedServiceViewHolder {
        val binding = ItemServiceParticipatedBinding.inflate(LayoutInflater.from(context), parent, false)
        return CompletedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedServiceViewHolder, position: Int) {
        servicesList!![position]?.let { holder.bind(context, it, viewModel) }
    }

    override fun getItemCount(): Int {
        return try {
            servicesList!!.size
        } catch (e: Exception) {
            Log.d("completed service list failed", "${e.message}")
            0
        }

    }
}