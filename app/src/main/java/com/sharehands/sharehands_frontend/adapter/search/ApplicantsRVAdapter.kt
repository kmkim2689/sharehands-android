package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.sharehands.sharehands_frontend.view.search.ApplicantInfoActivity
import com.sharehands.sharehands_frontend.view.search.RecruitActivity
import com.sharehands.sharehands_frontend.viewmodel.search.RecruitViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicantsRVAdapter(private val context: RecruitActivity, private val applicantsList: java.util.ArrayList<Participated?>?,
                          private val token: String,
                          private val serviceId: Int,
                          private val viewModel: RecruitViewModel, private val isExpired: Boolean)
    :RecyclerView.Adapter<ApplicantsRVAdapter.ApplicantsViewHolder>() {
        inner class ApplicantsViewHolder(private val binding: ItemUserAppliedBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: Participated) {
                    val view = (context as RecruitActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_check_applicants)
                    val applicantsNum = context.findViewById<TextView>(R.id.tv_current_num_applied)
                    Glide.with(context as RecruitActivity)
                        .load(current.profileUrl)
                        .into(binding.ivUserProfile)

                    binding.tvUserNickname.text = current.name
                    binding.tvLevelNum.text = current.level.toString()
                    binding.tvUserRating.text = current.rating.toString()

                    if (current.isPermission == true) {
                        binding.btnPermitReview.visibility = View.GONE
                        binding.btnReject.visibility = View.GONE
                    } else {
                        if (isExpired) {
                            binding.btnReject.visibility = View.GONE
                            binding.btnPermitReview.visibility = View.VISIBLE
                        } else {
                            binding.btnReject.visibility = View.VISIBLE
                            binding.btnPermitReview.visibility = View.GONE
                        }
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

                    binding.btnReject.setOnClickListener {
                        if ((current.userId?.toInt() ?: 0) != 0) {
                            RetrofitClient.createRetorfitClient().rejectUser(token, current.userId!!, serviceId.toLong())
                                .enqueue(object : Callback<Void> {
                                    override fun onResponse(
                                        call: Call<Void>,
                                        response: Response<Void>
                                    ) {
                                        if (response.code() == 200) {
                                            Log.d("거절 성공", "${response.code()}")
                                            try {
                                                // 이렇게 해야 오류가 안남
                                                applicantsList?.remove(current)
                                                notifyItemRemoved(adapterPosition)
                                                notifyItemRangeChanged(adapterPosition, applicantsList!!.size)
                                                val size = applicantsList.size
                                                applicantsNum.text = size.toString()
                                                Snackbar.make(view, "지원을 거절하였습니다.", 1000).show()
                                            } catch (e: Exception) {
                                                Snackbar.make(view, "알 수 없는 오류가 발생하였습니다.", 1000).show()
                                            }
                                        } else {
                                            Log.d("거절 실패", "${response.code()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        Log.d("거절 실패", "${t.message}")
                                    }

                                })
                        }
                    }

                    itemView.setOnClickListener {
                        val userId = current.userId
                        val intent = Intent(context as RecruitActivity, ApplicantInfoActivity::class.java)
                        intent.putExtra("userId", userId)
                        context.startActivity(intent)
//                        if (userId != null) {
//                            RetrofitClient.createRetorfitClient().getUserProfile(token, userId)
//                                .enqueue(object : Callback<UserProfile> {
//                                    override fun onResponse(
//                                        call: Call<UserProfile>,
//                                        response: Response<UserProfile>
//                                    ) {
//                                        val result = response.body()
//                                        if (response.isSuccessful && result != null) {
//                                            val profileDialog = DialogProfile(context as RecruitActivity, userId)
//                                            profileDialog.show(result)
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<UserProfile>, t: Throwable) {
//
//                                    }
//
//                                })
//                        }



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