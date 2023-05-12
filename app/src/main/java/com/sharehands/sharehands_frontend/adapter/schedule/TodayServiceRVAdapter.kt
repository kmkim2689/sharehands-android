package com.sharehands.sharehands_frontend.adapter.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.databinding.ItemTodayServiceBinding
import com.sharehands.sharehands_frontend.network.schedule.TodayService

class TodayServiceRVAdapter(private val context: Context, private val serviceList: List<TodayService>)
    : RecyclerView.Adapter<TodayServiceRVAdapter.TodayServiceViewHolder>() {
        inner class TodayServiceViewHolder(private val binding: ItemTodayServiceBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: TodayService) {
                    binding.tvTodayTime.text = current.time.toString()
                    binding.tvTodayLocation.text = current.location.toString()
                    binding.tvTodayTitle.text = current.workName.toString()

                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayServiceViewHolder {
        val binding = ItemTodayServiceBinding.inflate(LayoutInflater.from(context), parent, false)
        return TodayServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayServiceViewHolder, position: Int) {
        holder.bind(context, serviceList[position])
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}