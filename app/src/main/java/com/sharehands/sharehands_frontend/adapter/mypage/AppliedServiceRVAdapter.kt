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
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.network.mypage.RecruitedServices
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.mypage.AppliedServiceActivity
import com.sharehands.sharehands_frontend.view.mypage.RecruitedServiceActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class AppliedServiceRVAdapter(private val context: AppliedServiceActivity, private val viewModel: ServiceMgtViewModel,
                              private val servicesList: ArrayList<RecruitedService>?)
    :RecyclerView.Adapter<AppliedServiceRVAdapter.RecruitedServiceViewHolder>() {
        inner class RecruitedServiceViewHolder(val binding: ItemServiceSearchBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: RecruitedService, position: Int, viewModel: ServiceMgtViewModel) {
                val token = SharedPreferencesManager.getInstance(context)
                    .getString("token", "null")
                val btnApply = binding.ivApplyQuick
                val btnCancel = binding.ivApplyCancel
                // TODO int로 보내야하는가?
                val serviceId = current.serviceId.toInt()
                // 스낵바 띄울 액티비티
                val view = (context as AppliedServiceActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_applied)
                val total = (context as AppliedServiceActivity).findViewById<TextView>(R.id.tv_total_applied)

                var totalNum = viewModel.appliedNum.value?.toInt()

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

                val isApplied = current.userApplied
                Log.d("isApplied", "${isApplied}")

                btnApply.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE


                btnCancel.setOnClickListener {
                    viewModel.cancelService(token, serviceId)
                    // 데이터를 관찰하는 코드를 넣어주어야 삭제가 제대로 반영됨
                    viewModel.isCancelSuccessful.observe(context as AppliedServiceActivity) {
                        if (viewModel.isCancelSuccessful.value == true) {
                            try {
                                Log.d("success", "true")
                                servicesList?.remove(current)
                                notifyItemRemoved(adapterPosition)
                                notifyItemRangeChanged(adapterPosition, servicesList!!.size)
                                totalNum = totalNum?.minus(1)
                                total.text = "총 ${totalNum}개의 봉사에 지원하였습니다."
                                Snackbar.make(view, "봉사활동 지원을 취소하였습니다.", 1000).show()
                            } catch (e: Exception) {
                                Snackbar.make(view, "알 수 없는 오류가 발생하였습니다.", 1000).show()
                            }

                        } else {
                            Log.d("success", "false")
                            Snackbar.make(view, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", 1000).show()

                        }
                    }

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
        val binding = ItemServiceSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecruitedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecruitedServiceViewHolder, position: Int) {
        holder.bind(context!!, servicesList!![position], position, viewModel)
    }

    override fun getItemCount(): Int {
        return servicesList!!.size
    }
}