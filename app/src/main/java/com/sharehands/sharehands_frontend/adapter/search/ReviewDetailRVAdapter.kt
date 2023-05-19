package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemReviewBinding
import com.sharehands.sharehands_frontend.network.search.Review
import com.sharehands.sharehands_frontend.network.search.ReviewDetailItem
import com.sharehands.sharehands_frontend.view.BlockActivity
import com.sharehands.sharehands_frontend.view.ReportReviewActivity
import com.sharehands.sharehands_frontend.view.search.ReviewDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ReviewDetailViewModel

class ReviewDetailRVAdapter(private val context: ReviewDetailActivity, private val reviewList: ArrayList<ReviewDetailItem>?,
                            private val token: String,
                            private val serviceId: Int, private val viewModel: ReviewDetailViewModel)
    : RecyclerView.Adapter<ReviewDetailRVAdapter.ReviewDetailViewHolder>(){
        inner class ReviewDetailViewHolder(val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: ReviewDetailItem) {
                if (current.isAuthor == true) {
                    binding.ivBan.visibility = View.INVISIBLE
                    binding.ivReport.visibility = View.INVISIBLE
                } else {
                    binding.ivBan.visibility = View.VISIBLE
                    binding.ivReport.visibility = View.VISIBLE
                }

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

                Glide.with(context)
                    .load(current.profileUrl)
                    .into(binding.ivUserProfile)

                binding.tvUserNickname.text = current.memberName
                binding.tvLevelNum.text = current.level.toString()

                // 별점
                val rating = current.rateAvg
                binding.layoutRating.rating = rating.toFloat()

                binding.tvReviewContent.text = current.content

                // 신고
                binding.ivReport.setOnClickListener {
                    val reviewId = current.reviewId
                    val reportIntent = Intent(context as ReviewDetailActivity, ReportReviewActivity::class.java)
                    reportIntent.putExtra("reviewId", reviewId)
                    context.startActivity(reportIntent)
                }

                // 차단
                binding.ivBan.setOnClickListener {
                    val userId = current.userId
                    val banIntent = Intent(context as ReviewDetailActivity, BlockActivity::class.java)
                    banIntent.putExtra("userId", userId)
                    context.startActivity(banIntent)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewDetailViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ReviewDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewDetailViewHolder, position: Int) {
        holder.bind(context, reviewList!![position])
    }

    override fun getItemCount(): Int {
        return reviewList!!.size
    }
}