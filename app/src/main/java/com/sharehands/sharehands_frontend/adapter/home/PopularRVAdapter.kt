package com.sharehands.sharehands_frontend.adapter.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceRankingBinding
import com.sharehands.sharehands_frontend.network.home.PopularItem
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.search.ServiceDetailActivity

class PopularRVAdapter(private val context: MainActivity, private val list: List<PopularItem>)
    : RecyclerView.Adapter<PopularRVAdapter.PopularViewHolder>() {
        inner class PopularViewHolder(private val binding: ItemServiceRankingBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: PopularItem) {
                    itemView.setOnClickListener {
                        val serviceId = current.serviceId
                        val intent = Intent(context as MainActivity, ServiceDetailActivity::class.java)
                        intent.putExtra("serviceId", serviceId.toInt())
                        context.startActivity(intent)
                    }
                    binding.executePendingBindings()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = DataBindingUtil.inflate<ItemServiceRankingBinding>(LayoutInflater.from(parent.context), R.layout.item_service_ranking, parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}