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
import com.sharehands.sharehands_frontend.databinding.ItemUserAppliedBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ApplicantsData
import com.sharehands.sharehands_frontend.network.search.Participated
import com.sharehands.sharehands_frontend.network.search.UserProfile
import com.sharehands.sharehands_frontend.view.search.RecruitActivity
import com.sharehands.sharehands_frontend.viewmodel.search.RecruitViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicantsRVAdapter(private val context: RecruitActivity, private val applicantsList: List<Participated?>?,
                          private val token: String,
                          private val serviceId: Int,
                          private val viewModel: RecruitViewModel)
    :RecyclerView.Adapter<ApplicantsRVAdapter.ApplicantsViewHolder>() {
        inner class ApplicantsViewHolder(private val binding: ItemUserAppliedBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: Participated) {
                    val view = (context as RecruitActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_check_applicants)

                    Glide.with(context as RecruitActivity)
                        .load(current.profileUrl)
                        .into(binding.ivUserProfile)

                    binding.tvUserNickname.text = current.name
                    binding.tvLevelNum.text = current.level.toString()
                    binding.tvUserRating.text = current.rating.toString()

                    if (current.isPermission == true) {
                        binding.btnPermitReview.visibility = View.GONE
                    } else {
                        binding.btnPermitReview.visibility = View.VISIBLE
                    }

                    binding.btnPermitReview.setOnClickListener {
                        viewModel.allowReview(token, current.userId!!.toLong(), serviceId.toLong())
                        viewModel.isAllowSuccessful.observe(context) {
                            if (viewModel.isAllowSuccessful.value == true) {
                                binding.btnPermitReview.visibility = View.GONE
                                val snackbarSugSuccess = Snackbar.make(view, "사용자에 리뷰를 허용하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugSuccess.show()
                            } else {
                                val snackbarSugFail = Snackbar.make(view, "리뷰 허용에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugFail.show()
                            }
                        }
                    }

                    itemView.setOnClickListener {
                        val userId = current.userId
                        if (userId != null) {
                            RetrofitClient.createRetorfitClient().getUserProfile(token, userId)
                                .enqueue(object : Callback<UserProfile> {
                                    override fun onResponse(
                                        call: Call<UserProfile>,
                                        response: Response<UserProfile>
                                    ) {
                                        val result = response.body()
                                        if (response.isSuccessful && result != null) {
                                            val profileDialog = DialogProfile(context as RecruitActivity, userId)
                                            profileDialog.show(result)
                                        }
                                    }

                                    override fun onFailure(call: Call<UserProfile>, t: Throwable) {

                                    }

                                })
                        }



                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantsViewHolder {
        val binding = ItemUserAppliedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ApplicantsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicantsViewHolder, position: Int) {
        holder.bind(context, applicantsList!![position]!!)
    }

    override fun getItemCount(): Int {
        return applicantsList!!.size
    }

}