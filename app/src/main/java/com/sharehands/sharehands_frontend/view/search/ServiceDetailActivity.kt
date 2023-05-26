package com.sharehands.sharehands_frontend.view.search

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ReviewPreviewRVAdapter
import com.sharehands.sharehands_frontend.adapter.search.ServiceImageVPAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityServiceDetailBinding
import com.sharehands.sharehands_frontend.databinding.DialogProfileBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.ServiceContent
import com.sharehands.sharehands_frontend.network.search.UserProfile
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.BlockActivity
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.ProgressDialog
import com.sharehands.sharehands_frontend.view.ReportServiceActivity
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

        Log.d("serviceId", "${serviceId}")

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

        // 네트워크 통신이 5초 안에 이뤄지지 않는다면 그냥 없앰
//        Handler().postDelayed({
//            layout.visibility = View.VISIBLE
//            layout.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this@ServiceDetailActivity,
//                    R.anim.anim_element_fade_in
//                )
//            )
//            progressDialog.dismiss()
//        }, 500)


        // 네트워크 통신 성공 시, 마지막에 progressDialog.dismiss() 코드 추가할것

        binding.btnBack.setOnClickListener {
            finish()
        }


        if (token != "null" && serviceId != 0) {
            val progressDialog = ProgressDialog(this, "콘텐츠 로드 중")
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog.setCancelable(false)
            progressDialog.show()
            viewModel.showContents(token, serviceId)
            viewModel.contents.observe(this) {
                val photoList = viewModel.contents.value!!.photoList
                if (viewModel.contents.value != null) {
                    progressDialog.dismiss()
                    layout.visibility = View.VISIBLE
                    layout.startAnimation(
                        AnimationUtils.loadAnimation(
                        this@ServiceDetailActivity,
                            R.anim.anim_element_fade_in
                        )
                    )
                    val viewPagerAdapter = ServiceImageVPAdapter(this@ServiceDetailActivity,
                        photoList
                    )
                    viewPager.adapter = viewPagerAdapter
                    TabLayoutMediator(binding.tabLayout, viewPager)  { tab, position ->
                        viewPager.setCurrentItem(tab.position)
                    }.attach()
                } else {
                    progressDialog.dismiss()
                    finish()
                }


                Glide.with(this)
                    .load(viewModel.contents.value?.profileUrl.toString())
                    .into(binding.ivUserProfile)

                val reviewAdapter = ReviewPreviewRVAdapter(this, viewModel.contents.value?.reviewLists)
                Log.d("review contents", "${viewModel.contents.value?.reviewLists}")
                val reviewLayoutManager = LinearLayoutManager(this)
                binding.rvReviewsPreview.adapter = reviewAdapter
                binding.rvReviewsPreview.layoutManager = reviewLayoutManager
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



        binding.layoutUserProfile.setOnClickListener {
            viewModel.getProfile(token, viewModel.contents.value?.userId!!.toInt())
        }

        /*
        * 이 코드에서는 binding.layoutUserProfile를 클릭할 때마다 viewModel.getProfile을 호출하고 viewModel.profile 값을 옵저빙하며, viewModel.profile 값이 변경될 때마다 ProfileDialog가 여러번 생성되고 보여지게 됩니다. 이러한 이유로 다이얼로그를 닫으려면 여러 번 클릭해야 하는 문제가 발생합니다.

해결 방법으로는 viewModel.profile 값을 옵저빙할 때마다 ProfileDialog를 생성하고 보여주는 것이 아니라, viewModel.profile 값이 변경될 때 한번만 ProfileDialog를 생성하고 보여주도록 구현해야 합니다. 다음과 같이 viewModel.profile 값을 옵저빙하고, 값이 변경될 때 ProfileDialog를 생성하도록 코드를 수정할 수 있습니다.
* 이렇게 수정하면 binding.layoutUserProfile을 클릭할 때마다 viewModel.getProfile을 호출하고 viewModel.profile 값을 옵저빙하며, viewModel.profile 값이 변경될 때 한번만 ProfileDialog가 생성되고 보여지게 됩니다.
        * */

        viewModel.profile.observe(this) {
            val profile = ProfileDialog(this, viewModel)
            profile.show(viewModel.profile.value!!)
        }

        viewModel.isAuthor.observe(this) {
            if (viewModel.isAuthor.value == true) {
                binding.tvReportTitle.visibility = View.GONE
                binding.viewSeparator4.visibility = View.GONE
                binding.btnRecruitCloseClient.visibility = View.GONE
                binding.btnApplyCancel.visibility = View.GONE
                binding.btnApply.visibility = View.GONE
                if (viewModel.contents.value!!.isExpired || viewModel.contents.value!!.isFull) {
                    binding.btnRecruitCloseAuthor.visibility = View.VISIBLE
                    binding.btnRecruit.visibility = View.GONE
                } else {
                    binding.btnRecruitCloseAuthor.visibility = View.GONE
                    binding.btnRecruit.visibility = View.VISIBLE
                }

            } else {
                binding.btnRecruitCloseAuthor.visibility = View.GONE
                binding.btnRecruit.visibility = View.GONE
                if (viewModel.contents.value!!.isFull) {
                    if (viewModel.contents.value!!.isExpired) {
                        binding.btnRecruitCloseClient.visibility = View.VISIBLE
                        binding.btnApplyCancel.visibility = View.GONE
                        binding.btnApply.visibility = View.GONE
                    } else {
                        if (viewModel.contents.value?.didApply == true) {
                            Log.d("didapply", "${viewModel.contents.value?.didApply}")
                            binding.btnApplyCancel.visibility = View.VISIBLE
                            binding.btnApply.visibility = View.GONE
                            binding.btnRecruitCloseClient.visibility = View.GONE
                        } else {
                            Log.d("didapply", "${viewModel.contents.value?.didApply}")
                            binding.btnApplyCancel.visibility = View.GONE
                            binding.btnApply.visibility = View.GONE
                            binding.btnRecruitCloseClient.visibility = View.VISIBLE
                        }
                    }

                } else {
                    if (viewModel.contents.value?.didApply == true) {
                        Log.d("didapply", "${viewModel.contents.value?.didApply}")
                        binding.btnApplyCancel.visibility = View.VISIBLE
                        binding.btnApply.visibility = View.GONE
                        binding.btnRecruitCloseClient.visibility = View.GONE
                    } else {
                        Log.d("didapply", "${viewModel.contents.value?.didApply}")
                        binding.btnApplyCancel.visibility = View.GONE
                        binding.btnApply.visibility = View.VISIBLE
                        binding.btnRecruitCloseClient.visibility = View.GONE
                    }
                }

            }
        }

        viewModel.isLiked.observe(this) {
            if (viewModel.isLiked.value == true) {
                binding.ivLikeFilled.visibility = View.VISIBLE
                binding.ivLikeUnfilled.visibility = View.GONE
            } else {
                binding.ivLikeFilled.visibility = View.GONE
                binding.ivLikeUnfilled.visibility = View.VISIBLE
            }
        }

        viewModel.isScraped.observe(this) {
            if (viewModel.isScraped.value == true) {
                Log.d("isscraped", "${viewModel.isScraped.value}")
                binding.ivScrapFilled.visibility = View.VISIBLE
                binding.ivScrapUnfilled.visibility = View.GONE
            } else {
                Log.d("isscraped", "${viewModel.isScraped.value}")
                binding.ivScrapFilled.visibility = View.GONE
                binding.ivScrapUnfilled.visibility = View.VISIBLE
            }
        }


        binding.btnApply.setOnClickListener {
            viewModel.apply(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("봉사활동에 지원하였습니다.")
                    snackbar.show()
                    binding.btnApply.visibility = View.GONE
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
                    binding.btnApplyCancel.visibility = View.GONE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }

        binding.ivScrapUnfilled.setOnClickListener {
            viewModel.scrap(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                Log.d("issuccessful", "${viewModel.isSuccessful.value}")
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("게시글을 스크랩하였습니다.")
                    snackbar.show()
                    binding.ivScrapFilled.visibility = View.VISIBLE
                    binding.ivScrapUnfilled.visibility = View.GONE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }

        binding.ivScrapFilled.setOnClickListener {
            viewModel.scrapCancel(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("게시글 스크랩을 취소하였습니다.")
                    snackbar.show()
                    binding.ivScrapUnfilled.visibility = View.VISIBLE
                    binding.ivScrapFilled.visibility = View.GONE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }

        binding.ivLikeUnfilled.setOnClickListener {
            viewModel.postLike(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("게시글에 좋아요를 눌렀습니다.")
                    snackbar.show()
                    binding.ivLikeUnfilled.visibility = View.GONE
                    binding.ivLikeFilled.visibility = View.VISIBLE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }

        binding.ivLikeFilled.setOnClickListener {
            viewModel.cancelLike(token, serviceId)
            viewModel.isSuccessful.observe(this) {
                if (viewModel.isSuccessful.value == true) {
                    val snackbar = makeSnackbar("게시글 좋아요를 취소하였습니다.")
                    snackbar.show()
                    binding.ivLikeUnfilled.visibility = View.VISIBLE
                    binding.ivLikeFilled.visibility = View.GONE
                } else {
                    val snackbar = makeSnackbar("네트워크 오류가 발생하였습니다.")
                    snackbar.show()
                }
            }
        }

        binding.tvReviewTitle.setOnClickListener {
            val reviewIntent = Intent(this, ReviewDetailActivity::class.java)
            reviewIntent.putExtra("serviceId", serviceId)
            startActivity(reviewIntent)
        }

        binding.btnRecruit.setOnClickListener {
            val recruitIntent = Intent(this, RecruitActivity::class.java)
            recruitIntent.putExtra("serviceId", serviceId)
            startActivity(recruitIntent)
        }

        binding.btnRecruitCloseAuthor.setOnClickListener {
            val recruitIntent = Intent(this, RecruitActivity::class.java)
            recruitIntent.putExtra("serviceId", serviceId)
            startActivity(recruitIntent)
        }

        binding.tvReportTitle.setOnClickListener {
            val reportIntent = Intent(this, ReportServiceActivity::class.java)
            reportIntent.putExtra("serviceId", serviceId.toLong())
            startActivity(reportIntent)
        }
    }




    fun makeSnackbar(text: String): Snackbar {
        return Snackbar.make(binding.coordinatorLayout, text, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}

class ProfileDialog(private val context: AppCompatActivity, private val viewModel: ServiceDetailViewModel) {
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
            val userId = viewModel.contents.value?.userId
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