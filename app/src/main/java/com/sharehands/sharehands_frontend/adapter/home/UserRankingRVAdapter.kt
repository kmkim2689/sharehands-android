package com.sharehands.sharehands_frontend.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemUserRankingBinding
import com.sharehands.sharehands_frontend.network.home.RankingItem
import com.sharehands.sharehands_frontend.view.MainActivity

class UserRankingRVAdapter(private val context: MainActivity, private val list: List<RankingItem>)
    : RecyclerView.Adapter<UserRankingRVAdapter.UserRankingViewHolder>(){
        inner class UserRankingViewHolder(private val binding: ItemUserRankingBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: RankingItem) {
                    when (current.ranking.toInt()) {
                        1 -> {
                            binding.ivRanking.setImageResource(R.drawable.ic_rank_first)
                        }
                        2 -> {
                            binding.ivRanking.setImageResource(R.drawable.ic_rank_second)
                        }
                        3 -> {
                            binding.ivRanking.setImageResource(R.drawable.ic_rank_third)
                        }
                        else -> {
                            binding.ivRanking.setImageResource(R.drawable.ic_rank_etc)
                        }
                    }
                    binding.tvRankingNum.text = current.ranking.toString()
                    binding.tvNickname.text = current.nickname
                    binding.tvUserLevelNum.text = current.level.toString()
                    binding.tvCount.text = "${current.count.toString()}회"


                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankingViewHolder {
        val binding = DataBindingUtil.inflate<ItemUserRankingBinding>(LayoutInflater.from(parent.context), R.layout.item_user_ranking, parent, false)
        return UserRankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserRankingViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}