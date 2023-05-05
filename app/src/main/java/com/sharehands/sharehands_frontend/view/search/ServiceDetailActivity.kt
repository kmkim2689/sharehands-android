package com.sharehands.sharehands_frontend.view.search

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServiceImageVPAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding
import com.sharehands.sharehands_frontend.view.ProgressDialog

class ServiceDetailActivity:AppCompatActivity() {
    lateinit var binding: ActivityServiceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        binding = DataBindingUtil.setContentView<ActivityServiceDetailBinding>(this, R.layout.activity_service_detail)

        val layout = binding.coordinatorLayout
        val viewPager = binding.viewpagerThumbnails

        // TODO 테스트용 imageurl 리스트 삭제
        val imageUrls = ArrayList<String>()
        imageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Elon_Musk_Royal_Society.jpg/225px-Elon_Musk_Royal_Society.jpg")
        imageUrls.add("https://ichef.bbci.co.uk/news/640/cpsprodpb/D42F/production/_116391345_tes1.png")
        val viewPagerAdapter = ServiceImageVPAdapter(this, imageUrls)
        viewPager.adapter = viewPagerAdapter

        layout.visibility = View.INVISIBLE

        val progressDialog = ProgressDialog(this)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        progressDialog.setCancelable(false)
        progressDialog.show()

        // 네트워크 통신이 5초 안에 이뤄지지 않는다면 그냥 없앰
        Handler().postDelayed({
            layout.visibility = View.VISIBLE
            layout.startAnimation(
                AnimationUtils.loadAnimation(
                    this@ServiceDetailActivity,
                    R.anim.anim_element_fade_in
                )
            )
            progressDialog.dismiss()
        }, 3000)

        // 네트워크 통신 성공 시, 마지막에 progressDialog.dismiss() 코드 추가할것

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}