package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceOnRecruitBinding
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.network.mypage.RecruitedServices
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.mypage.RecruitedServiceActivity
import com.sharehands.sharehands_frontend.view.search.RecruitActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class RecruitedServiceRVAdapter(private val context: RecruitedServiceActivity, private val viewModel: ServiceMgtViewModel,
                                private val servicesList: ArrayList<RecruitedService>?)
    :RecyclerView.Adapter<RecruitedServiceRVAdapter.RecruitedServiceViewHolder>() {
        inner class RecruitedServiceViewHolder(val binding: ItemServiceOnRecruitBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: RecruitedService, position: Int, viewModel: ServiceMgtViewModel) {
                val token = SharedPreferencesManager.getInstance(context)
                    .getString("token", "null")

                val total = (context as RecruitedServiceActivity).findViewById<TextView>(R.id.tv_total_recruited)

                val recruitBtn = binding.ivGotoRecruit
                val deleteBtn = binding.ivDeleteRecruit

                val serviceId = current.serviceId.toInt()

                Glide.with(context)
                    .load(current.imageUrl)
                    .into(binding.ivServiceRecruitThumbnail)

                Glide.with(context)
                    .load(current.profileUrl)
                    .into(binding.ivServiceRecruitUser)

                binding.tvServiceRecruitUser.text = current.nickName
                binding.tvRecruitTitle.text = current.serviceName
                val locationList = current.location.split(" ")
                if (locationList.size >= 2) {
                    val district = locationList[1]
                    binding.tvRecruitLocation.text = district
                }
                binding.tvRecruitPeriod.text = current.date
                binding.tvRecruitPeople.text = "${current.maxNum}명"
                binding.tvRecruitDay.text = current.dow

                val view = (context as RecruitedServiceActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_recruit)

                var totalNum = viewModel.recruitedNum.value?.toInt()

                val isApplied = current.userApplied
                Log.d("isApplied", "${isApplied}")

                val isExpired = current.isExpired
                if (isExpired) {
                    deleteBtn.visibility = View.GONE
                }


                deleteBtn.setOnClickListener {
                    viewModel.deleteService(token, serviceId)
                    viewModel.isDeleteSuccessful.observe(context as RecruitedServiceActivity) {
                        if (viewModel.isDeleteSuccessful.value == true) {
                            Log.d("success", "true")
                            try {
                                // 이렇게 해야 오류가 안남
                                servicesList?.remove(current)
                                notifyItemRemoved(adapterPosition)
                                totalNum = totalNum?.minus(1)
                                notifyItemRangeChanged(adapterPosition, servicesList!!.size)
                                total.text = "총 ${totalNum}개의 봉사를 모집하였습니다."
                                Snackbar.make(view, "봉사활동을 삭제하였습니다.", 1000).show()
                            } catch (e: Exception) {
                                Snackbar.make(view, "알 수 없는 오류가 발생하였습니다.", 1000).show()
                            }


                        } else {
                            Snackbar.make(view, "네트워크 상 오류가 발생하였습니다. 다시 시도해주세요", 1000).show()
                        }
                    }
                }

                recruitBtn.setOnClickListener {
                    Log.d("클릭한 봉사활동 expired 여부", "$isExpired")
                    val recruitIntent = Intent(itemView.context, RecruitActivity::class.java)
                    recruitIntent.putExtra("isExpired", isExpired)
                    recruitIntent.putExtra("serviceId", serviceId)
                    itemView.context.startActivity(recruitIntent)
                }

                itemView.setOnClickListener {
                    Log.d("클릭된 봉사활동 아이디", serviceId.toString())
                    val intent = Intent(itemView.context, ServiceDetailActivity::class.java)
                    intent.putExtra("serviceId", serviceId)
                    itemView.context.startActivity(intent)
                }


            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitedServiceViewHolder {
        val binding = ItemServiceOnRecruitBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecruitedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecruitedServiceViewHolder, position: Int) {
        holder.bind(context!!, servicesList!![position], position, viewModel)
    }

    override fun getItemCount(): Int {
        return servicesList!!.size
    }
}