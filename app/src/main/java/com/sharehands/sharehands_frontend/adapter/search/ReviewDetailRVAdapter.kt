package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemReviewBinding
import com.sharehands.sharehands_frontend.network.search.Review
import com.sharehands.sharehands_frontend.view.search.ReviewDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ReviewDetailViewModel

class ReviewDetailRVAdapter(private val context: ReviewDetailActivity, private val reviewList: List<Review>?,
                            private val serviceId: Int, private val viewModel: ReviewDetailViewModel)
    : RecyclerView.Adapter<ReviewDetailRVAdapter.ReviewDetailViewHolder>(){
        inner class ReviewDetailViewHolder(val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: Review) {
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

                binding.ivBan.visibility = View.GONE
                binding.ivReport.visibility = View.GONE
                Glide.with(context)
                    .load(current.profileUrl)
                    .into(binding.ivUserProfile)

                binding.tvUserNickname.text = current.memberName
                binding.tvLevelNum.text = current.level.toString()

                // 별점
                val rating = current.rateAvg
                if (0 <= rating && rating < 0.5) {

                } else if (0.5 <= rating && rating < 1.5) {
                    binding.ivFirstStarFilled.visibility = View.VISIBLE
                    binding.ivFirstStarUnfilled.visibility = View.GONE
                } else if (1.5 <= rating && rating < 2.5) {
                    binding.ivFirstStarFilled.visibility = View.VISIBLE
                    binding.ivFirstStarUnfilled.visibility = View.GONE
                    binding.ivSecondStarFilled.visibility = View.VISIBLE
                    binding.ivSecondStarUnfilled.visibility = View.GONE
                } else if (2.5 <= rating && rating < 3.5) {
                    binding.ivFirstStarFilled.visibility = View.VISIBLE
                    binding.ivFirstStarUnfilled.visibility = View.GONE
                    binding.ivSecondStarFilled.visibility = View.VISIBLE
                    binding.ivSecondStarUnfilled.visibility = View.GONE
                    binding.ivThirdStarFilled.visibility = View.VISIBLE
                    binding.ivThirdStarUnfilled.visibility = View.GONE
                } else if (3.5 <= rating && rating < 4.5) {
                    binding.ivFirstStarFilled.visibility = View.VISIBLE
                    binding.ivFirstStarUnfilled.visibility = View.GONE
                    binding.ivSecondStarFilled.visibility = View.VISIBLE
                    binding.ivSecondStarUnfilled.visibility = View.GONE
                    binding.ivThirdStarFilled.visibility = View.VISIBLE
                    binding.ivThirdStarUnfilled.visibility = View.GONE
                    binding.ivFourthStarFilled.visibility = View.VISIBLE
                    binding.ivFourthStarUnfilled.visibility = View.GONE
                } else {
                    binding.ivFirstStarFilled.visibility = View.VISIBLE
                    binding.ivFirstStarUnfilled.visibility = View.GONE
                    binding.ivSecondStarFilled.visibility = View.VISIBLE
                    binding.ivSecondStarUnfilled.visibility = View.GONE
                    binding.ivThirdStarFilled.visibility = View.VISIBLE
                    binding.ivThirdStarUnfilled.visibility = View.GONE
                    binding.ivFourthStarFilled.visibility = View.VISIBLE
                    binding.ivFourthStarUnfilled.visibility = View.GONE
                    binding.ivFifthStarFilled.visibility = View.VISIBLE
                    binding.ivFifthStarUnfilled.visibility = View.GONE
                }

                binding.tvReviewContent.text = current.content
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