package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemServiceMgtBinding
import com.sharehands.sharehands_frontend.model.mypage.ServiceMgtMenu
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.mypage.*

class MyPageServiceRVAdapter(private val context: MainActivity): RecyclerView.Adapter<MyPageServiceRVAdapter.MyPageServiceViewHolder>() {
    val serviceMgtList: List<ServiceMgtMenu> = listOf(
        ServiceMgtMenu(R.drawable.ic_service_mgt_recruit, "모집한\n봉사"),
        ServiceMgtMenu(R.drawable.ic_service_mgt_applied, "지원한\n봉사"),
        ServiceMgtMenu(R.drawable.ic_service_mgt_participated, "참여한\n봉사"),
        ServiceMgtMenu(R.drawable.ic_service_mgt_scrap, "스크랩한\n봉사"),
        ServiceMgtMenu(R.drawable.ic_service_mgt_suggested, "제안받은\n봉사")
    )

    class MyPageServiceViewHolder(val binding: ItemServiceMgtBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, service: ServiceMgtMenu, position: Int) {
            binding.ivServiceMgt.setImageResource(service.drawable)
            binding.tvServiceMgt.text = service.title

            itemView.setOnClickListener {
                when (position) {
                    0 -> {
                        val intent = Intent(context as MainActivity, RecruitedServiceActivity::class.java)
                        context.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(context as MainActivity, AppliedServiceActivity::class.java)
                        context.startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(context as MainActivity, ParticipatedServiceActivity::class.java)
                        context.startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(context as MainActivity, ScrapedServiceActivity::class.java)
                        context.startActivity(intent)
                    }
                    4 -> {
                        val intent = Intent(context as MainActivity, SuggestedServiceActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageServiceViewHolder {
        val binding = ItemServiceMgtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageServiceViewHolder, position: Int) {
        holder.bind(context, serviceMgtList[position], position)
        // 간격 조정
        val layoutParams = holder.itemView.layoutParams
        layoutParams.width = 190
        holder.itemView.requestLayout()
    }

    override fun getItemCount(): Int {
        return serviceMgtList.size
    }
}