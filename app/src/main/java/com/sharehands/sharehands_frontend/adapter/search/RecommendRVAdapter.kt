package com.sharehands.sharehands_frontend.adapter.search

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.DialogProfileBinding
import com.sharehands.sharehands_frontend.databinding.ItemUserRecommendBinding
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.search.Participated
import com.sharehands.sharehands_frontend.network.search.Suggestion
import com.sharehands.sharehands_frontend.network.search.UserProfile
import com.sharehands.sharehands_frontend.view.BlockActivity
import com.sharehands.sharehands_frontend.view.search.ProfileDialog
import com.sharehands.sharehands_frontend.view.search.RecruitActivity
import com.sharehands.sharehands_frontend.viewmodel.search.RecruitViewModel
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceDetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendRVAdapter(private val context: RecruitActivity, private val recommentList: List<Suggestion?>?, private val serviceId: Long,
                         private val viewModel: RecruitViewModel, private val token: String)
    :RecyclerView.Adapter<RecommendRVAdapter.RecommendViewHolder>(){
        inner class RecommendViewHolder(private val binding: ItemUserRecommendBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: Suggestion) {
                    val view = (context as RecruitActivity).findViewById<CoordinatorLayout>(R.id.coordinator_layout_check_applicants)

                    Glide.with(context as RecruitActivity)
                        .load(current.profileUrl)
                        .into(binding.ivUserProfile)
                    binding.tvUserNickname.text = current.name
                    binding.tvLevelNum.text = current.level.toString()
                    binding.tvUserRating.text = current.rating.toString()

                    if (current.isInvited == true) {
                        binding.ivSuggestCancel.visibility = View.VISIBLE
                        binding.ivSuggestSelect.visibility = View.GONE
                    } else {
                        binding.ivSuggestCancel.visibility = View.GONE
                        binding.ivSuggestSelect.visibility = View.VISIBLE
                    }

                    binding.ivSuggestSelect.setOnClickListener {
                        viewModel.suggest(token, current.userId!!.toLong(), serviceId)
                        viewModel.isSuggestSuccessful.observe(context) {
                            if (viewModel.isSuggestSuccessful.value == true) {
                                binding.ivSuggestCancel.visibility = View.VISIBLE
                                binding.ivSuggestSelect.visibility = View.GONE
                                val snackbarSugSuccess = Snackbar.make(view, "사용자에게 봉사를 추천하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugSuccess.show()
                            } else {
                                val snackbarSugFail = Snackbar.make(view, "추천에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugFail.show()
                            }
                        }
                    }

                    binding.ivSuggestCancel.setOnClickListener {
                        viewModel.cancelSuggest(token, current.userId!!.toLong(), serviceId)
                        viewModel.isSuggestCancelSuccessful.observe(context) {
                            if (viewModel.isSuggestCancelSuccessful.value == true) {
                                binding.ivSuggestCancel.visibility = View.GONE
                                binding.ivSuggestSelect.visibility = View.VISIBLE
                                val snackbarSugSuccess = Snackbar.make(view, "사용자에 추천을 취소하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugSuccess.show()
                            } else {
                                val snackbarSugFail = Snackbar.make(view, "추천 취소에 실패하였습니다.", Snackbar.LENGTH_SHORT)
                                snackbarSugFail.show()
                            }
                        }
                    }

                    itemView.setOnClickListener {
                        val userId = current.userId
                        if (userId != null) {
                            RetrofitClient.createRetorfitClient().getUserProfile(token, userId)
                                .enqueue(object : Callback<UserProfile> {
                                    override fun onResponse(
                                        call: Call<UserProfile>,
                                        response: Response<UserProfile>
                                    ) {
                                        val result = response.body()
                                        if (response.isSuccessful && result != null) {
                                            val profileDialog = DialogProfile(context as RecruitActivity, userId)
                                            profileDialog.show(result)
                                        }
                                    }

                                    override fun onFailure(call: Call<UserProfile>, t: Throwable) {

                                    }

                                })
                        }



                    }
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = ItemUserRecommendBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(context, recommentList!![position]!!)
    }

    override fun getItemCount(): Int {
        return recommentList!!.size
    }
}

class DialogProfile(private val context: AppCompatActivity, private val userId: Long?) {
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