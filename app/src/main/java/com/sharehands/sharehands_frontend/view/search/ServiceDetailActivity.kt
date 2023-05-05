package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServiceImageVPAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.ProgressDialog
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceDetailViewModel

class ServiceDetailActivity:AppCompatActivity() {
    lateinit var binding: ActivityServiceDetailBinding
    lateinit var viewModel: ServiceDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        binding = DataBindingUtil.setContentView<ActivityServiceDetailBinding>(this, R.layout.activity_service_detail)

        val viewModel = ViewModelProvider(this).get(ServiceDetailViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        // TODO serviceId(workId) 몇 번부터 시작하는지 알아내기
        val serviceId = intent.getIntExtra("serviceId", 0)

        if (token == "null") {
            val snackbar = makeSnackbar("서비스를 이용하기 위하여 로그인이 필요합니다.")
            snackbar.setAction("로그인") {
                val intent = Intent(this, SocialLoginActivity::class.java)
                startActivity(intent)
            }
            snackbar.show()
            finish()
        }

        if (serviceId == 0) {
            val snackbar = makeSnackbar("잘못된 경로입니다. 다시 시도하세요.")
            snackbar.show()
            finish()
        }

        val viewPager = binding.viewpagerThumbnails
        val layout = binding.coordinatorLayout
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
        }, 1000)


        // 네트워크 통신 성공 시, 마지막에 progressDialog.dismiss() 코드 추가할것

        binding.btnBack.setOnClickListener {
            finish()
        }

        if (token != "null" && serviceId != 0) {
            val isSuccessful = viewModel.showContents(token, serviceId)
            if (isSuccessful) {
                Log.d("봉사활동 상세 화면 불러오기", "성공")

                // TODO 테스트용 imageurl 리스트(아래 세 줄 코드) 삭제
                // TODO 네트워크 통신 성공 시 viewpager adapter 초기화하고 할당하기
                val imageUrls = ArrayList<String>()
                imageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Elon_Musk_Royal_Society.jpg/225px-Elon_Musk_Royal_Society.jpg")
                imageUrls.add("https://ichef.bbci.co.uk/news/640/cpsprodpb/D42F/production/_116391345_tes1.png")

                val viewPagerAdapter = ServiceImageVPAdapter(this,
                    viewModel.contents.value?.photoList!!
                )
                viewPager.adapter = viewPagerAdapter
                // TODO 위의 dismiss와 충돌 시 해결방안
            } else {
                Log.d("봉사활동 상세 화면 불러오기", "실패")
                val snackbar = makeSnackbar("네트워크 문제가 발생하였습니다. 다시 시도해보세요.")
                snackbar.show()

            }

        }


    }



    fun makeSnackbar(text: String): Snackbar {
        return Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_SHORT)
    }

}