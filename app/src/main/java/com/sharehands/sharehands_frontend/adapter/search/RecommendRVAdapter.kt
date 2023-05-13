package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemUserRecommendBinding
import com.sharehands.sharehands_frontend.network.search.Participated
import com.sharehands.sharehands_frontend.network.search.Suggestion
import com.sharehands.sharehands_frontend.view.search.RecruitActivity
import com.sharehands.sharehands_frontend.viewmodel.search.RecruitViewModel

class RecommendRVAdapter(private val context: RecruitActivity, private val recommentList: List<Suggestion?>?, private val serviceId: Long,
                         private val viewModel: RecruitViewModel, private val token: String)
    :RecyclerView.Adapter<RecommendRVAdapter.RecommendViewHolder>(){
        inner class RecommendViewHolder(private val binding: ItemUserRecommendBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: Suggestion) {
                    val view = (context as RecruitActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_check_applicants)

                    Glide.with(context as RecruitActivity)
                        .load(current.profileUrl)
                        .into(binding.ivUserProfile)
                    binding.tvUserNickname.text = current.name
                    binding.tvLevelNum.text = current.level.toString()
                    binding.tvUserRating.text = current.rating.toString()

                    if (current.isInvited == true) {
                        binding.ivSuggestCancel.visibility = View.VISIBLE
                        binding.ivSuggestSelect.visibility = View.GONE
                    } else {
                        binding.ivSuggestCancel.visibility = View.GONE
                        binding.ivSuggestSelect.visibility = View.VISIBLE
                    }

                    binding.ivSuggestSelect.setOnClickListener {
                        viewModel.suggest(token, current.userId!!.toLong(), serviceId)
                        viewModel.isSuggestSuccessful.observe(context) {
                            if (viewModel.isSuggestSuccessful.value == true) {
                                binding.ivSuggestCancel.visibility = View.VISIBLE
                                binding.ivSuggestSelect.visibility = View.GONE
                                val snackbarSugSuccess = Snackbar.make(view, "사용자에게 봉사를 추천하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugSuccess.show()
                            } else {
                                val snackbarSugFail = Snackbar.make(view, "추천에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugFail.show()
                            }
                        }
                    }

                    binding.ivSuggestCancel.setOnClickListener {
                        viewModel.cancelSuggest(token, current.userId!!.toLong(), serviceId)
                        viewModel.isSuggestCancelSuccessful.observe(context) {
                            if (viewModel.isSuggestCancelSuccessful.value == true) {
                                binding.ivSuggestCancel.visibility = View.GONE
                                binding.ivSuggestSelect.visibility = View.VISIBLE
                                val snackbarSugSuccess = Snackbar.make(view, "사용자에 추천을 취소하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugSuccess.show()
                            } else {
                                val snackbarSugFail = Snackbar.make(view, "추천 취소에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugFail.show()
                            }
                        }
                    }
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = ItemUserRecommendBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(context, recommentList!![position]!!)
    }

    override fun getItemCount(): Int {
        return recommentList!!.size
    }
}