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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ServiceImageVPAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceContent
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.ProgressDialog
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceDetailActivity:AppCompatActivity() {
    lateinit var binding: ActivityServiceDetailBinding
    lateinit var viewModel: ServiceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)
        binding = DataBindingUtil.setContentView<ActivityServiceDetailBinding>(this, R.layout.activity_service_detail)

        val viewModel = ViewModelProvider(this).get(ServiceDetailViewModel::class.java)
        var userId: Long = 0

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
            viewModel.showContents(token, serviceId)
            if (viewModel.contents.value?.author == true) {

            }
//            RetrofitClient.createRetorfitClient().getService(token, serviceId)
//                .enqueue(object : Callback<ServiceContent> {
//                    override fun onResponse(
//                        call: Call<ServiceContent>,
//                        response: Response<ServiceContent>
//                    ) {
//                        if (response.isSuccessful) {
//                            if (response.code() == 200) {
//                                val result = response.body()
//                                Log.d("봉사활동 상세 불러오기 데이터 result 변수", "${result}")
//                                val photoList = result?.photoList
//                                val viewPagerAdapter = ServiceImageVPAdapter(this@ServiceDetailActivity,
//                                    photoList
//                                )
//                                viewPager.adapter = viewPagerAdapter
//                            } else {
//                                Log.d("봉사활동 상세 데이터 불러오기 실패", response.code().toString())
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ServiceContent>, t: Throwable) {
//                        Log.d("봉사활동 상세 데이터 불러오기 실패", t.message.toString())
//                    }
//                })

        }

        viewModel.isAuthor.observe(this) {
            if (viewModel.isAuthor.value == true) {
                binding.btnApplyCancel.visibility = View.GONE
                binding.btnApply.visibility = View.GONE
                binding.btnRecruit.visibility = View.VISIBLE
            } else {
                if (viewModel.contents.value?.didApply == true) {
                    Log.d("didapply", "${viewModel.contents.value?.didApply}")
                    binding.btnApplyCancel.visibility = View.VISIBLE
                    binding.btnApply.visibility = View.INVISIBLE
                    binding.btnRecruit.visibility = View.GONE
                } else {
                    Log.d("didapply", "${viewModel.contents.value?.didApply}")
                    binding.btnApplyCancel.visibility = View.INVISIBLE
                    binding.btnApply.visibility = View.VISIBLE
                    binding.btnRecruit.visibility = View.GONE
                }
            }
        }

        viewModel.photoList.observe(this) {
            val photoList = viewModel.photoList?.value
            if (photoList != null) {
                val viewPagerAdapter = ServiceImageVPAdapter(this@ServiceDetailActivity,
                    photoList
                )
                viewPager.adapter = viewPagerAdapter
            }


            Glide.with(this)
                .load(viewModel.contents.value?.profileUrl.toString())
                .into(binding.ivUserProfile)
        }

        binding.btnApply.setOnClickListener {
            viewModel.apply(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("봉사활동에 지원하였습니다.")
                    snackbar.show()
                    binding.btnApply.visibility = View.INVISIBLE
                    binding.btnApplyCancel.visibility = View.VISIBLE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()

                }
            }
        }

        binding.btnApplyCancel.setOnClickListener {
            viewModel.cancelApply(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("봉사활동에 지원을 취소하였습니다.")
                    snackbar.show()
                    binding.btnApply.visibility = View.VISIBLE
                    binding.btnApplyCancel.visibility = View.INVISIBLE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }
    }



    fun makeSnackbar(text: String): Snackbar {
        return Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_SHORT)
    }

}