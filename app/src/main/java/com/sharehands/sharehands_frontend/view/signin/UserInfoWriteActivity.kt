package com.sharehands.sharehands_frontend.view.signin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityUserInfoWriteBinding

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

        // 수정 폼 목록
        val name = binding.editName
        val nickname = binding.editNickname
        val email = binding.tvEmailContent
        val phone = binding.editPhoneContent
        val birthday = binding.editDayOfBirthday
        val address = binding.spinnerLocation

        val listOfViews = arrayOf(subtitle, title, nameTitle, nameDesc, nicknameTitle,
        nicknameDesc, emailTitle, emailDesc, phoneTitle, phoneDesc, birthdayTitle, birthdayDesc,
        locationTitle, locationSeoul, locationDesc, name, nickname, email, phone, birthday, address, nextBtnLayout)

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

        name.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.edit_text_fulfilled)
            } else {
                if (name!!.text.length in 2..15) {
                    v.setBackgroundResource(R.drawable.edit_text_fulfilled)
                } else {
                    v.setBackgroundResource(R.drawable.edit_text_not_fulfilled)
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