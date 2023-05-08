package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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

                val recruitBtn = binding.ivGotoRecruit
                val deleteBtn = binding.ivDeleteRecruit

                // TODO int로 보내야하는가?
                val serviceId = current.serviceId.toInt()
                // 스낵바 띄울 액티비티
                val activity = (context as RecruitedServiceActivity).findViewById<ConstraintLayout>(R.id.layout_main_activity)

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

                val isApplied = current.userApplied
                Log.d("isApplied", "${isApplied}")


                deleteBtn.setOnClickListener {
                    viewModel.deleteService(token, serviceId)
                    if (viewModel.isDeleteSuccessful.value == true) {
                        Log.d("success", "true")
                        val intent = Intent(RecruitedServiceActivity(), RecruitedServiceActivity::class.java)
                        RecruitedServiceActivity().finish()
                        RecruitedServiceActivity().startActivity(intent)
//                        val snackbarCancelSuccess = Snackbar.make(activity, "봉사활동 지원을 취소하였습니다.", Snackbar.LENGTH_SHORT)
//
//                        snackbarCancelSuccess.show()
                    } else {
//                        Log.d("success", "false")
//                        val snackbarCancelFail = Snackbar.make(activity, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
//                        snackbarCancelFail.show()
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