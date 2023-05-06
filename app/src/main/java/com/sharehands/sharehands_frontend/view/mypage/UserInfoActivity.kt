package com.sharehands.sharehands_frontend.view.mypage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityUserInfoBinding
import com.sharehands.sharehands_frontend.network.mypage.MyPageDetail
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.mypage.MyPageDetailViewModel
import kotlinx.coroutines.CoroutineScope

class UserInfoActivity: AppCompatActivity() {
    private lateinit var viewModel: MyPageDetailViewModel
    private lateinit var binding: ActivityUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MyPageDetailViewModel::class.java)
        binding.viewModel = viewModel

        var checked = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0)
        val chkEdu = binding.checkboxEducation
        val chkCulture = binding.checkboxCulture
        val chkHealth = binding.checkboxHealth
        val chkEnv = binding.checkboxEnvironment
        val chkTech = binding.checkboxTech
        val chkEtc = binding.checkboxEtc
        val nextBtnLayout = binding.layoutBtnNext
        val nextButtonInactive = binding.btnNextInactive
        val nextButtonActive = binding.btnNextActive
        val address = binding.spinnerLocation
        val checkboxes = listOf(chkEdu, chkCulture, chkHealth, chkEnv, chkTech, chkEtc)
        var interestsList = arrayListOf<String>()

        var chkList = arrayListOf<Int>(0, 0, 0, 0, 0, 0)

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")


        isValid(checkboxes, checked, nextButtonActive, nextButtonInactive)

        if (token != "null") {
            viewModel.getUserInfo(token)

        }


        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.location_spinner,
            android.R.layout.simple_spinner_item
        )

        // 드롭다운 시 레이아웃 설정
        spinnerAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item)
        // address(spinner 뷰)에 만들어놓은 adapter를 할당한다.
        address.adapter = spinnerAdapter
        address.dropDownVerticalOffset = 120
        when (viewModel.location.value) {
            "동대문구" -> address.setSelection(0)
            "중랑구" -> address.setSelection(1)
            "노원구" -> address.setSelection(2)
        }




        address.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var district = when (position) {
                    0 -> {
                        "동대문구"
                    }
                    1 -> {
                        "중랑구"
                    }
                    else -> {
                        "노원구"
                    }
                }
                Log.d("선택된 관심지역", district)
                viewModel.setLocation(district)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않는 경우는 발생하지 않으므로 비워둠
            }
        }

//        var currentInterests = viewModel.interests.value
//        Log.d("currentInterests", "${currentInterests}")
//        if (currentInterests != null) {
//            for (elem in currentInterests) {
//                Log.d("elem", "${elem}")
//                when(elem) {
//                    "교육" -> {
//                        chkEdu.isChecked = true
//                    }
//                    "문화" -> {
//                        chkEdu.isChecked = true
//                    }
//                    "보건" -> {
//                        chkEdu.isChecked = true
//                    }
//                    "환경" -> {
//                        chkEdu.isChecked = true
//                    }
//                    "기술" -> {
//                        chkEdu.isChecked = true
//                    }
//                    "기타" -> {
//                        chkEdu.isChecked = true
//                    }
//                }
//            }
//        }

        viewModel.interests.observe(this) { interests ->
            Log.d("currentInterests", "$interests")
            interests?.let {
                for (elem in it) {
                    Log.d("elem", "$elem")
                    when (elem) {
                        "교육" -> chkEdu.isChecked = true
                        "문화" -> chkEdu.isChecked = true
                        "보건" -> chkEdu.isChecked = true
                        "환경" -> chkEdu.isChecked = true
                        "기술" -> chkEdu.isChecked = true
                        "기타" -> chkEdu.isChecked = true
                    }
                }
            }
        }

        val observer = Observer<MyPageDetail?> {
            val progress = viewModel.result.value?.reviewScore
            val progressBar = binding.progressBarRating
            if (progress != null) {
                progressBar.progress = (progress * 10).toInt()
                Log.d("progress", "${progressBar.progress}")
            }
            progressBar.max = 50


        }
        viewModel.result.observe(this, observer)

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        binding.btnNextActive.setOnClickListener {
            for (i in 0 until checkboxes.size) {
                if (i == 0) {
                    if (checkboxes[i].isChecked)
                    interestsList.add("교육")
                } else if (i == 1) {
                    if (checkboxes[i].isChecked)
                    interestsList.add("문화")
                }  else if (i == 2) {
                    if (checkboxes[i].isChecked)
                    interestsList.add("보건")
                } else if (i == 3) {
                    if (checkboxes[i].isChecked)
                    interestsList.add("환경")
                } else if (i == 4) {
                    if (checkboxes[i].isChecked)
                    interestsList.add("기술")
                } else {
                    if (checkboxes[i].isChecked)
                    interestsList.add("기타")
                }
            }
            Log.d("interests lists changed", "${interestsList}")
            if (token != "null") {
                viewModel.post(token, interestsList)
                viewModel.isSuccessful.observe(this) {
                    if (viewModel.isSuccessful.value == true) {
                        Log.d("정보 수정 작성 완료", "${viewModel.isSuccessful.value}")
                        showSnackbar("게시글 작성이 완료되었습니다.")
                        finish()
                    } else {
                        Log.d("정보 수정 실패", "${viewModel.isSuccessful.value}")
                        // TODO 스낵바 띄우기
                        showSnackbar("네트워크 오류가 발생하였습니다. 다시 시도해보세요.")
                    }
                }
            }



        }
    }

    private fun isValid(chkboxList: List<CheckBox>, checkedList: ArrayList<Int>, activeButton: TextView, inactiveButton: TextView) {
        for ((i, elem) in chkboxList.withIndex()) {
            elem.setOnClickListener {
                if (elem.isChecked) {
                    checkedList[i] = 1
                } else {
                    checkedList[i] = 0
                }

                if (checkedList.contains(1)) {
                    activeButton.apply {
                        if (visibility == View.INVISIBLE) {
                            visibility = View.VISIBLE
                            startAnimation(
                                AnimationUtils.loadAnimation(
                                    this@UserInfoActivity,
                                    R.anim.fade_in
                                )
                            )
                        }
                    }
                    inactiveButton.visibility = View.INVISIBLE
                } else {
                    inactiveButton.apply {
                        if (visibility == View.INVISIBLE) {
                            visibility = View.VISIBLE
                            startAnimation(
                                AnimationUtils.loadAnimation(
                                    this@UserInfoActivity,
                                    R.anim.fade_in
                                )
                            )
                        }
                    }
                    activeButton.visibility = View.INVISIBLE
                }
            }
        }
    }
    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}