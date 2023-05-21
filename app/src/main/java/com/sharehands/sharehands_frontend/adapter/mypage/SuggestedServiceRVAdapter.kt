package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceSuggestedBinding
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.mypage.SuggestedServiceActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuggestedServiceRVAdapter(private val context: SuggestedServiceActivity, private val viewModel: ServiceMgtViewModel,
                                private val serviceList: ArrayList<RecruitedService?>?)
    : RecyclerView.Adapter<SuggestedServiceRVAdapter.SuggestedServiceViewHolder>(){
        inner class SuggestedServiceViewHolder(val binding: ItemServiceSuggestedBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, position: Int, current: RecruitedService) {

                    val view = (context as SuggestedServiceActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_suggested)
                    val token = SharedPreferencesManager.getInstance(context).getString("token", "null")
                    Glide.with(context)
                        .load(current.imageUrl)
                        .into(binding.ivServicePreviewThumbnail)

                    Glide.with(context)
                        .load(current.profileUrl)
                        .into(binding.ivServicePreviewUser)

                    binding.tvServicePreviewUser.text = current.nickName
                    binding.tvPreviewTitle.text = current.serviceName
                    binding.tvPreviewLocation.text = current.location
                    binding.tvPreviewPeriod.text = current.date
                    binding.tvPreviewPeople.text = current.maxNum.toString()
                    binding.tvPreviewDay.text = current.dow

                    itemView.setOnClickListener {
                        val detailIntent = Intent(context, ServiceDetailActivity::class.java)
                        Log.d("serviceId", "${current.serviceId.toInt()}")
                        detailIntent.putExtra("serviceId", current.serviceId.toInt())
                        context.startActivity(detailIntent)
                    }

                    binding.ivAccept.setOnClickListener {
                        if (token != "null") {
                            CoroutineScope(Dispatchers.Main).launch {
                                val result = viewModel.acceptService(token, current.serviceId)
                                if (result == true) {
                                    Log.d("수락 성공", "${result}")
                                    serviceList?.removeAt(adapterPosition)
                                    notifyItemRemoved(adapterPosition)
                                    notifyItemRangeChanged(adapterPosition, serviceList!!.size)
                                    Snackbar.make(view, "봉사활동을 수락하였습니다.", 1000).show()
                                } else {
                                    Snackbar.make(view, "봉사활동을 수락에 실패하였습니다.", 1000).show()
                                }
                            }
                        }

                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedServiceViewHolder {
        val binding = ItemServiceSuggestedBinding.inflate(LayoutInflater.from(context), parent, false)
        return SuggestedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestedServiceViewHolder, position: Int) {
        holder.bind(context, position, serviceList!![position]!!)
    }

    override fun getItemCount(): Int {
        return serviceList!!.size
    }
}