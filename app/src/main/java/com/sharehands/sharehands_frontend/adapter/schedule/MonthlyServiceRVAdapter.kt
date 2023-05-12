package com.sharehands.sharehands_frontend.adapter.schedule

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.databinding.ItemMonthlyScheduleBinding
import com.sharehands.sharehands_frontend.network.schedule.MonthlyService
import com.sharehands.sharehands_frontend.view.schedule.MonthlyCalendarActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity

class MonthlyServiceRVAdapter(val context: Context, val serviceList: List<MonthlyService>)
    :RecyclerView.Adapter<MonthlyServiceRVAdapter.MonthlyServiceViewHolder>(){
        inner class MonthlyServiceViewHolder(val binding: ItemMonthlyScheduleBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(current: MonthlyService) {
                    binding.tvScheduleTitle.text = current.workName
                    var dDayNum = ""
                    if (current.dDay == 0) {
                        dDayNum = "-Day"
                    } else if (current.dDay > 0) {
                        dDayNum = "+${current.dDay}"
                    } else {
                        dDayNum = current.dDay.toString()
                    }
                    binding.tvScheduleDdayNum.text = dDayNum
                    binding.tvScheduleLocation.text = current.location
                    binding.tvScheduleStartTime.text = current.time
                    binding.tvScheduleMonth.text = current.month.toString()
                    binding.tvScheduleDay.text = current.day.toString()


                    val serviceId = current.workId

                    itemView.setOnClickListener {
                        val intent = Intent(context as MonthlyCalendarActivity, ServiceDetailActivity::class.java)
                        intent.putExtra("serviceId", serviceId)
                        (context as MonthlyCalendarActivity).startActivity(intent)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyServiceViewHolder {
        val binding = ItemMonthlyScheduleBinding.inflate(LayoutInflater.from(context), parent, false)
        return MonthlyServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyServiceViewHolder, position: Int) {
        holder.bind(serviceList[position])
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}