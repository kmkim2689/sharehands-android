package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceParticipatedBinding
import com.sharehands.sharehands_frontend.databinding.ItemServiceScrapBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.mypage.RecruitedService
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.mypage.ParticipatedServiceActivity
import com.sharehands.sharehands_frontend.view.mypage.ScrapedServiceActivity
import com.sharehands.sharehands_frontend.view.search.ReviewWriteActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.mypage.ServiceMgtViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapedServiceRVAdapter(private val context: ScrapedServiceActivity, private val viewModel: ServiceMgtViewModel,
                              private val servicesList: ArrayList<RecruitedService?>?)
    : RecyclerView.Adapter<ScrapedServiceRVAdapter.ScrapedServiceViewHolder>(){
        inner class ScrapedServiceViewHolder(private val binding: ItemServiceScrapBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: RecruitedService, viewModel: ServiceMgtViewModel) {
                    val token = SharedPreferencesManager.getInstance(context).getString("token", "null")
                    val cancelBtn = binding.ivScrapCancel
                    val serviceId = current.serviceId.toInt()

                    var totalNum = viewModel.scrapedNum.value?.toInt()

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

                    val view = (context as ScrapedServiceActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_scrap)
                    val total = (context as ScrapedServiceActivity).findViewById<TextView>(R.id.tv_total_scraped)

                    itemView.setOnClickListener {
                        val detailIntent = Intent(context, ServiceDetailActivity::class.java)
                        Log.d("serviceId", "${current.serviceId.toInt()}")
                        detailIntent.putExtra("serviceId", current.serviceId.toInt())
                        context.startActivity(detailIntent)
                    }

                    cancelBtn.setOnClickListener {
                        // 스크랩 취소 api
                        viewModel.cancelScrap(token, serviceId)
                        viewModel.isScrapCancelSuccessful.observe(context as ScrapedServiceActivity) {
                            if (viewModel.isScrapCancelSuccessful.value == true) {
                                try {
                                    // 이렇게 해야 삭제 시 오류가 나지 않음
                                    servicesList?.remove(current)
                                    notifyItemRemoved(adapterPosition)
                                    notifyItemRangeChanged(adapterPosition, servicesList!!.size)
                                    Log.d("스크랩 취소", "성공")
                                    totalNum = totalNum?.minus(1)
                                    total.text = "총 ${totalNum}개의 봉사를 스크랩하였습니다."
                                    Snackbar.make(view, "스크랩을 취소하였습니다.", 1000).show()
                                } catch (e: Exception) {
                                    Snackbar.make(view, "알 수 없는 오류가 발생하였습니다.", 1000).show()
                                }

                            } else {
                                Log.d("스크랩 취소", "실패")
                                Snackbar.make(view, "스크랩을 취소할 수 없습니다.", 1000).show()
                            }
                        }

                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapedServiceRVAdapter.ScrapedServiceViewHolder {
        val binding = ItemServiceScrapBinding.inflate(LayoutInflater.from(context), parent, false)
        return ScrapedServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapedServiceRVAdapter.ScrapedServiceViewHolder, position: Int) {
        servicesList!![position]?.let { holder.bind(context, it, viewModel) }
    }

    override fun getItemCount(): Int {
        return try {
            servicesList!!.size
        } catch (e: Exception) {
            Log.d("스크랩 service list failed", "${e.message}")
            0
        }

    }
}