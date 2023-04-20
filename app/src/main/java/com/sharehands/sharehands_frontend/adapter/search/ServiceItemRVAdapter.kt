package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.model.search.ServiceItem
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity

class ServiceItemRVAdapter(private val context: MainActivity, private val serviceList: ArrayList<ServiceItem>): RecyclerView.Adapter<ServiceItemRVAdapter.ServiceItemViewHolder>() {
    class ServiceItemViewHolder(private val binding: ItemServiceSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: ServiceItem, position: Int) {

            val serviceId = item.serviceId

            // 썸네일
            Glide.with(context)
                .load(item.thumbnail)
                .into(binding.ivServicePreviewThumbnail)

            // 프로필
            Glide.with(context)
                .load(item.userImage)
                .into(binding.ivServicePreviewUser)

            binding.tvServicePreviewUser.text = item.userNickname
            binding.tvPreviewTitle.text = item.title
            binding.tvPreviewLocation.text = item.location
            binding.tvPreviewPeriod.text = item.period
            binding.tvPreviewPeople.text = item.maxNum
            binding.tvPreviewDay.text = item.day

            if (item.isCertified) {
                binding.ivPreviewCert.visibility = View.VISIBLE
            } else {
                binding.ivPreviewCert.visibility = View.GONE
            }

            if (item.isApplied) {
                binding.ivApplyCancel.visibility = View.VISIBLE
                binding.ivApplyQuick.visibility = View.INVISIBLE
            } else {
                binding.ivApplyCancel.visibility = View.INVISIBLE
                binding.ivApplyQuick.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                val intent = Intent(context as MainActivity, ServiceDetailActivity::class.java)
                // TODO 아이디 넘겨주기
                intent.putExtra("serviceId", serviceId)
                context.startActivity(intent)
            }

            // 지원하기 버튼 눌렀을 때 API 동작
            binding.ivApplyQuick.setOnClickListener {
                // TODO 통신 성공 시, 보여주는 버튼 변경
            }

            binding.ivApplyCancel.setOnClickListener {
                // TODO 통신 성공 시, 보여주는 버튼 변경
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        val binding = ItemServiceSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        holder.bind(context, serviceList[position], position)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}