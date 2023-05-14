package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.search.SearchResultActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.search.SearchKeywordViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel

class SearchKeywordRVAdapter(private val context: SearchResultActivity?, private val viewModel: SearchKeywordViewModel, private val serviceList: ArrayList<ServiceList>?): RecyclerView.Adapter<SearchKeywordRVAdapter.ServicesSearchViewHolder>() {

    class ServicesSearchViewHolder(private val binding: ItemServiceSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, current: ServiceList, position: Int, viewModel: SearchKeywordViewModel, serviceList: ArrayList<ServiceList>?) {
            val token = SharedPreferencesManager.getInstance(context)
                .getString("token", "null")
            val btnApply = binding.ivApplyQuick
            val btnCancel = binding.ivApplyCancel
            // TODO int로 보내야하는가?
            val serviceId = current.workId.toInt()
            // 스낵바 띄울 액티비티

            val view = (context as SearchResultActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_result)

            Glide.with(context)
                .load(current.imageUrl)
                .into(binding.ivServicePreviewThumbnail)

            Glide.with(context)
                .load(current.profileUrl)
                .into(binding.ivServicePreviewUser)

            binding.tvServicePreviewUser.text = current.nickName
            binding.tvPreviewTitle.text = current.workName
            val locationList = current.location.split(" ")
            if (locationList.size >= 2) {
                val district = locationList[1]
                binding.tvPreviewLocation.text = district
            }
            binding.tvPreviewPeriod.text = current.date
            binding.tvPreviewPeople.text = current.maxNum
            binding.tvPreviewDay.text = current.dow

            val isApplied = current.userApplied
            Log.d("isApplied", "${isApplied}")

            val isAuthor = current.isAuthor
            if (isAuthor) {
                btnApply.visibility = View.GONE
                btnCancel.visibility = View.GONE
            } else {
                if (isApplied) {
                    btnApply.visibility = View.GONE
                    btnCancel.visibility = View.VISIBLE
                } else {
                    btnApply.visibility = View.VISIBLE
                    btnCancel.visibility = View.GONE
                }
            }

//            // TODO 봉사활동 클릭 이벤트 설정하기
            btnApply.setOnClickListener {
                viewModel.applyService(token, serviceId)
                if (viewModel.isApplySuccessful.value == true) {
                    Log.d("success", "true")
                    btnApply.visibility = View.GONE
                    btnCancel.visibility = View.VISIBLE
                    val intent = Intent(context, MainActivity::class.java)
                    serviceList!![position].userApplied = true
                    val snackbarApplySuccess = Snackbar.make(view, "봉사활동에 지원하였습니다.", Snackbar.LENGTH_SHORT)
                    snackbarApplySuccess.show()
                } else {
                    val snackbarApplyFail = Snackbar.make(view, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
                    snackbarApplyFail.show()
                    Log.d("success", "false")

                }

            }

            btnCancel.setOnClickListener {
                viewModel.cancelService(token, serviceId)
                if (viewModel.isCancelSuccessful.value == true) {
                    Log.d("success", "true")
                    btnApply.visibility = View.VISIBLE
                    btnCancel.visibility = View.GONE
                    serviceList!![position].userApplied = true
                    val snackbarApplySuccess = Snackbar.make(view, "봉사활동 지원을 취소하였습니다.", Snackbar.LENGTH_SHORT)
                    snackbarApplySuccess.show()
//                    val snackbarCancelSuccess = Snackbar.make(mainActivity, "봉사활동 지원을 취소하였습니다.", Snackbar.LENGTH_SHORT)
//                    snackbarCancelSuccess.show()
                } else {
                    Log.d("success", "false")
                    val snackbarCancelFail = Snackbar.make(view, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
                    snackbarCancelFail.show()
//                    val snackbarCancelFail = Snackbar.make(mainActivity, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
//                    snackbarCancelFail.show()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesSearchViewHolder {
        val binding = ItemServiceSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicesSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicesSearchViewHolder, position: Int) {
        holder.bind(context!!, serviceList!![position], position, viewModel, serviceList)
    }

    override fun getItemCount(): Int {
        return serviceList!!.size
    }


}