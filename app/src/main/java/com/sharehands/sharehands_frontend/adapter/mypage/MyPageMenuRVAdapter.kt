package com.sharehands.sharehands_frontend.adapter.mypage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.databinding.ItemEtcServiceBinding
import com.sharehands.sharehands_frontend.model.mypage.MyPageMenu
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.mypage.AppInfoActivity
import com.sharehands.sharehands_frontend.view.mypage.ContactActivity
import com.sharehands.sharehands_frontend.view.mypage.FAQActivity
import com.sharehands.sharehands_frontend.view.mypage.NoticeActivity

class MyPageMenuRVAdapter(private val context: MainActivity): RecyclerView.Adapter<MyPageMenuRVAdapter.MyPageMenuViewHolder>() {
    val menuList = listOf<MyPageMenu>(
        MyPageMenu("공지사항", "업데이트 및 이벤트 관련 정보를 제공합니다."),
        MyPageMenu("앱 정보", "이용 약관과 사용 정보를 제공합니다."),
        MyPageMenu("FAQ", "사용자가 자주 묻는 질문들과 답변을 제공합니다."),
        MyPageMenu("문의하기", "쉐어핸즈의 운영자에게 직접 문의할 수 있습니다.")
    )

    class MyPageMenuViewHolder(val binding: ItemEtcServiceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, menu: MyPageMenu, position: Int) {
            binding.tvServiceTitle.text = menu.title
            binding.tvServiceDetail.text = menu.desc
            itemView.setOnClickListener {
                when (position) {
                    0 -> {
                        val intent = Intent(context as MainActivity, NoticeActivity::class.java)
                        context.startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(context as MainActivity, AppInfoActivity::class.java)
                        context.startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(context as MainActivity, FAQActivity::class.java)
                        context.startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(context as MainActivity, ContactActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageMenuViewHolder {
        val binding = ItemEtcServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageMenuViewHolder, position: Int) {
        holder.bind(context, menuList[position], position)


    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}