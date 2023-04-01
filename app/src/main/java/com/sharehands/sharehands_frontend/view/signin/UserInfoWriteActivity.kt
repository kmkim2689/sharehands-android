package com.sharehands.sharehands_frontend.view.signin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityUserInfoWriteBinding
import java.util.*
import kotlin.collections.ArrayList

class UserInfoWriteActivity: AppCompatActivity() {
    lateinit var binding: ActivityUserInfoWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info_write)

        // 애니메이션
        val subtitle = binding.tvInfoSubtitle
        val title = binding.tvInfoTitle
        val nameTitle = binding.tvName
        val nameDesc = binding.tvNameDescription
        val nicknameTitle = binding.tvNickname
        val nicknameDesc = binding.tvNicknameDescription
        val emailTitle = binding.tvEmail
        val emailDesc = binding.tvEmailDescription
        val phoneTitle = binding.tvPhone
        val phoneDesc = binding.tvPhoneDescription
        val birthdayTitle = binding.tvDayOfBirthday
        val birthdayDesc = binding.tvDayOfBirthdayDesc
        val locationTitle = binding.tvLocation
        val locationSeoul = binding.tvLocationSeoul
        val locationDesc = binding.tvLocationDesc
        val nextBtnLayout = binding.layoutBtnNext
        val nextBtnActive = binding.btnNextActive
        val nextBtnInactive = binding.btnNextInactive
        val nextBtnList: List<TextView> = listOf(nextBtnActive, nextBtnInactive)

        // 수정 폼 목록
        val name = binding.editName
        val nickname = binding.editNickname
        val email = binding.tvEmailContent
        val phone = binding.editPhoneContent
        val birthday = binding.editDayOfBirthday
        val address = binding.spinnerLocation

        // 폼 관리(유효성 검사). 이름, 닉네임, 휴대폰, 생일 순서
        val status = arrayListOf<Boolean>(false, false, false, false)

        val listOfViews = arrayOf(subtitle, title, nameTitle, nameDesc, nicknameTitle,
        nicknameDesc, emailTitle, emailDesc, phoneTitle, phoneDesc, birthdayTitle, birthdayDesc,
        locationTitle, locationSeoul, locationDesc, name, nickname, email, phone, birthday, address, nextBtnLayout)

        // 스피너의 항목을 설정하는 방법
        // string.xml 파일에서 <string-array>륾 만들고 name 속성을 설정한다. 그 안에 <item>들을 넣는다.
        // 어댑터를 만든다.
        // R.layout.simple_spinner_item과 R.layout.simple_spinner_dropdown_item은 안드로이드에서 기본적으로 제공하는 Spinner 드롭다운 메뉴의 뷰를 정의한 레이아웃 파일
        //
        //simple_spinner_item : Spinner에서 선택된 항목을 보여줄 때 사용되는 뷰입니다.
        //simple_spinner_dropdown_item : Spinner에서 항목을 선택할 수 있는 드롭다운 뷰에서 사용되는 뷰입니다.

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

        var district = "동대문구"

        address.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                district = when (position) {
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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않는 경우는 발생하지 않으므로 비워둠
            }

        }

        listOfViews.forEach {
            it.visibility = View.INVISIBLE
        }

        val group1: List<View> = listOf(subtitle, title)
        val group2: List<View> = listOf(nameTitle, nameDesc, nicknameTitle,
            nicknameDesc, emailTitle, emailDesc, phoneTitle, phoneDesc, birthdayTitle, birthdayDesc,
            locationTitle, locationSeoul, locationDesc, name, nickname, email, phone, birthday, address)
        val group3: List<View> = listOf(nextBtnLayout)

        appear(group1, 500)
        appear(group2, 1000)
        appear(group3, 1500)

        binding.btnBack.setOnClickListener {
            finish()
        }


        // edittext에서 전화번호 포맷 지정하는 방법
        // 1. edittext의 최대 길이 제한(하이픈 포함)
        // 2. edittext의 inputType을 phone로 설정
        // 3. addTextChangedListener 적용.
        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 전화번호 형식 적용
                if (s != null && s.length >= 3) {
                    if (s.length == 4 || s.length == 9) {
                        phone.setText("${s.substring(0, s.length - 1)}-${s.substring(s.length - 1)}")
                        phone.setSelection(phone.length())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        focusEffect(name, 2, 15)
        focusEffect(nickname, 2, 15)
        focusEffect(phone, 13, 13)
        focusEffect(birthday, 8, 8)

        isValid(name, 2, 15, 0, status, nextBtnList)
        isValid(nickname, 2, 15, 1, status, nextBtnList)
        isValid(phone, 13, 13, 2, status, nextBtnList)
        isValid(birthday, 8, 8, 3, status, nextBtnList)

    }

    private fun isValid(editText: EditText, minLength: Int, maxLength: Int, index: Int, status: ArrayList<Boolean>, nextBtnList: List<TextView>) {
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (minLength <= maxLength) {
                    status[index] = editText!!.text.length in minLength..maxLength
                    var result = true
                    for (elem in status) {
                        if (elem == false) {
                            result = false
                        }
                    }
                    if (result == true) {
                        nextBtnList[0].apply {
                            if (visibility == View.INVISIBLE) {
                                visibility = View.VISIBLE
                                startAnimation(
                                    AnimationUtils.loadAnimation(
                                        this@UserInfoWriteActivity,
                                        R.anim.fade_in
                                    )
                                )
                            }
                        }
                        nextBtnList[1].apply {
                            visibility = View.INVISIBLE
                        }
                    } else {
                        nextBtnList[0].apply {
                            visibility = View.INVISIBLE
                        }
                        nextBtnList[1].apply {
                            visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun focusEffect(editText: EditText, minLength: Int, maxLength: Int) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.edit_text_fulfilled)
            } else {
                if (minLength <= maxLength) {
                    if (editText!!.text.length in minLength..maxLength) {
                        v.setBackgroundResource(R.drawable.edit_text_fulfilled)
                    } else {
                        v.setBackgroundResource(R.drawable.edit_text_not_fulfilled)
                    }
                }
            }
        }
    }


    private fun appear(viewList: List<View>, delayTime: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            for (elem in viewList) {
                elem.apply {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@UserInfoWriteActivity,
                            R.anim.anim_element_fade_in
                        )
                    )
                }
            }
        }, delayTime)
    }
}