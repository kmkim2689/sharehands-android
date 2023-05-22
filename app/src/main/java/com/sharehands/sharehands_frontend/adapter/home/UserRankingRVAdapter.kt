package com.sharehands.sharehands_frontend.adapter.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.DialogProfile
import com.sharehands.sharehands_frontend.databinding.DialogProfileBinding
import com.sharehands.sharehands_frontend.databinding.ItemUserRankingBinding
import com.sharehands.sharehands_frontend.network.home.RankingItem
import com.sharehands.sharehands_frontend.network.search.UserProfile
import com.sharehands.sharehands_frontend.view.BlockActivity
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.search.RecruitActivity

class UserRankingRVAdapter(private val context: MainActivity, private val list: List<RankingItem>)
    : RecyclerView.Adapter<UserRankingRVAdapter.UserRankingViewHolder>() {
    inner class UserRankingViewHolder(private val binding: ItemUserRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

            itemView.setOnClickListener {
                val profileDialog = DialogProfile(context as MainActivity, current.userId)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankingViewHolder {
        val binding = DataBindingUtil.inflate<ItemUserRankingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user_ranking,
            parent,
            false
        )
        return UserRankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserRankingViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class DialogProfileMain(private val context: AppCompatActivity, private val userId: Long?) {
        private val dialog = Dialog(context)
        private var binding: DialogProfileBinding

        init {
            binding = DialogProfileBinding.inflate(LayoutInflater.from(context.applicationContext))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.setContentView(binding.root)
            // 다이얼로그 가로 길이 화면에 맞추기 위하여 사용
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
            val deviceWidth = size.x
            params?.width = (deviceWidth).toInt()
            dialog?.window?.attributes = params as WindowManager.LayoutParams
            binding.ivExit.setOnClickListener {
                dialog.dismiss()
            }

            binding.btnDialogBan.setOnClickListener {
                if (userId != null) {
                    val blockIntent = Intent(context, BlockActivity::class.java)
                    blockIntent.putExtra("userId", userId.toLong())
                    context.startActivity(blockIntent)
                }
            }
        }

        fun show(userProfile: UserProfile) {
            // 주의 : 레이아웃에서 databinding을 사용하지 않아야 제대로 값이 들어가게 된다.
            binding.tvDialogNickname.text = userProfile.nickname
            binding.tvDialogLevel.text = "나눔레벨 ${userProfile.level}"
            binding.tvDialogLocation.text = userProfile.location
            binding.tvDialogRating.text = userProfile.avgRate.toString()
            binding.tvNumRecruited.text = userProfile.managedWork.toString()
            binding.tvNumApplied.text = userProfile.appliedWork.toString()
            binding.tvNumParticipated.text = userProfile.participatedWork.toString()
            if (userProfile.author) {
                binding.btnDialogBan.visibility = View.GONE
            }
            Log.d("profile profile url", "${userProfile.profileUrl}")
            Glide.with(context)
                .load(userProfile.profileUrl)
                .into(binding.ivDialogUserProfile)

            // TODO 프로필 URL 호출하기

            dialog.show()
        }

    }
}