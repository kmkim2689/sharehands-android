package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemReviewBinding
import com.sharehands.sharehands_frontend.network.search.Review
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity

class ReviewPreviewRVAdapter(private val context: ServiceDetailActivity, private val reviewList: List<Review>?)
    : RecyclerView.Adapter<ReviewPreviewRVAdapter.ReviewPreviewViewHolder>() {
    inner class ReviewPreviewViewHolder(private val binding: ItemReviewBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, current: Review) {
            // 여기서는 신고, 차단 못하고 미리보기만 가능
            val dateTime = current.updatedDate.split("T")
            if (dateTime.size >= 2) {
                val date = dateTime[0]
                binding.tvDate.text = date
                val timeDetail = dateTime[1].split(".")
                if (timeDetail.size >= 2) {
                    val time = timeDetail[0]
                    binding.tvTime.text = time
                }
            }

            binding.ivBan.visibility = View.GONE
            binding.ivReport.visibility = View.GONE
            Glide.with(context)
                .load(current.profileUrl)
                .into(binding.ivUserProfile)

            binding.tvUserNickname.text = current.memberName
            binding.tvLevelNum.text = current.level.toString()

            // 별점
            val rating = current.rateAvg
            binding.layoutRating.rating = rating.toFloat()

            binding.tvReviewContent.text = current.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewPreviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ReviewPreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewPreviewViewHolder, position: Int) {
        if (reviewList != null) {
            holder.bind(context, reviewList[position])
        }
    }

    override fun getItemCount(): Int {
        return reviewList!!.size
    }
}