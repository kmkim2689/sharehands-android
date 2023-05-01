package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.databinding.ItemServiceSearchBinding
import com.sharehands.sharehands_frontend.model.search.ServiceItem
import com.sharehands.sharehands_frontend.view.MainActivity

class ServicesSearchRVAdapter(private val context: MainActivity, private val viewModel: ViewModel, private val serviceList: ArrayList<ServiceItem>?): RecyclerView.Adapter<ServicesSearchRVAdapter.ServicesSearchViewHolder>() {

    class ServicesSearchViewHolder(private val binding: ItemServiceSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, current: ServiceItem, position: Int) {
            Glide.with(context)
                .load(current.thumbnail)
                .into(binding.ivServicePreviewThumbnail)
            // TODO 나머지 요소들 값 설정하기
            // TODO 지원하기/취소하기 버튼 클릭 이벤트 설정하기
            // TODO 봉사활동 클릭 이벤트 설정하기
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesSearchViewHolder {
        val binding = ItemServiceSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicesSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicesSearchViewHolder, position: Int) {
        holder.bind(context, serviceList!![position], position)
    }

    override fun getItemCount(): Int {
        return serviceList!!.size
    }
}