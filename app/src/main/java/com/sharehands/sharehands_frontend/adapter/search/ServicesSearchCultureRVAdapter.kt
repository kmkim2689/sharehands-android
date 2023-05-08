package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.content.Intent
import android.text.Layout
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
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceList
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchCultureViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchEduViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceSearchViewModel
import retrofit2.Call
import javax.security.auth.callback.Callback

class ServicesSearchCultureRVAdapter(private val context: MainActivity?, private val viewModel: ServiceSearchCultureViewModel, private val serviceList: ArrayList<ServiceList>?): RecyclerView.Adapter<ServicesSearchCultureRVAdapter.ServicesSearchViewHolder>() {

    class ServicesSearchViewHolder(private val binding: ItemServiceSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, current: ServiceList, position: Int, viewModel: ServiceSearchCultureViewModel) {
            val token = SharedPreferencesManager.getInstance(context)
                .getString("token", "null")
            val btnApply = binding.ivApplyQuick
            val btnCancel = binding.ivApplyCancel
            // TODO int로 보내야하는가?
            val serviceId = current.workId.toInt()
            // 스낵바 띄울 액티비티
            val mainActivity = (context as MainActivity).findViewById<ConstraintLayout>(R.id.layout_main_activity)

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
            binding.tvPreviewPeople.text = "${current.maxNum}명"
            binding.tvPreviewDay.text = current.dow

            val isApplied = current.userApplied
            Log.d("isApplied", "${isApplied}")

            if (isApplied) {
                btnApply.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE
            } else {
                btnApply.visibility = View.VISIBLE
                btnCancel.visibility = View.GONE
            }

//            // TODO 봉사활동 클릭 이벤트 설정하기
            btnApply.setOnClickListener {
                viewModel.applyService(token, serviceId)
                if (viewModel.isApplySuccessful.value == true) {
                    Log.d("success", "true")
                    btnApply.visibility = View.GONE
                    btnCancel.visibility = View.VISIBLE
//                    val intent = Intent(context, MainActivity::class.java)
//                    (context as MainActivity).finish()
//                    (context as MainActivity).startActivity(intent)
                    val snackbarApplySuccess = Snackbar.make(mainActivity, "봉사활동에 지원하였습니다.", Snackbar.LENGTH_SHORT)
                    snackbarApplySuccess.show()
                } else {
                    Log.d("success", "false")
                    val snackbarApplyFail = Snackbar.make(mainActivity, "네트워크 문제로 지원에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
                    snackbarApplyFail.show()
                }

            }

            btnCancel.setOnClickListener {
                viewModel.cancelService(token, serviceId)
                if (viewModel.isCancelSuccessful.value == true) {
                    Log.d("success", "true")
                    btnApply.visibility = View.VISIBLE
                    btnCancel.visibility = View.GONE
//                    val intent = Intent(context, MainActivity::class.java)
//                    (context as MainActivity).finish()
//                    (context as MainActivity).startActivity(intent)
                    val snackbarCancelSuccess = Snackbar.make(mainActivity, "봉사활동 지원을 취소하였습니다.", Snackbar.LENGTH_SHORT)
                    snackbarCancelSuccess.show()
                } else {
                    Log.d("success", "false")
                    val snackbarCancelFail = Snackbar.make(mainActivity, "네트워크 문제로 취소에 실패하였습니다. 다시 시도해보세요.", Snackbar.LENGTH_SHORT)
                    snackbarCancelFail.show()
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
        holder.bind(context!!, serviceList!![position], position, viewModel)
    }

    override fun getItemCount(): Int {
        return serviceList!!.size
    }


}