package com.sharehands.sharehands_frontend.view.signin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityUserPreferencesBinding
import com.sharehands.sharehands_frontend.view.MainActivity

class UserPreferencesActivity: AppCompatActivity() {
    lateinit var binding: ActivityUserPreferencesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_preferences)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_preferences)

        var checked = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0)

        val subtitle = binding.tvInfoSubtitle
        val title = binding.tvInfoTitle
        val desc = binding.tvInterestsTitle
        val checkboxGroup = binding.layoutCategories
        val chk_edu = binding.checkboxEducation
        val chk_culture = binding.checkboxCulture
        val chk_health = binding.checkboxHealth
        val chk_env = binding.checkboxEnvironment
        val chk_tech = binding.checkboxTech
        val chk_overseas = binding.checkboxOverseas
        val chk_campaign = binding.checkboxCampaign
        val chk_disaster = binding.checkboxDisaster
        val chk_etc = binding.checkboxEtc
        val nextBtnLayout = binding.layoutBtnNext
        val nextButtonInactive = binding.btnNextInactive
        val nextButtonActive = binding.btnNextActive

        val group1 = listOf(subtitle, title)
        val group2 = listOf(desc, checkboxGroup)
        val group3 = listOf(nextBtnLayout)

        val checkboxes = listOf(chk_edu, chk_culture, chk_health, chk_env, chk_tech,
            chk_overseas, chk_campaign, chk_disaster, chk_etc)
        val views = listOf(subtitle, title, desc, checkboxGroup, nextBtnLayout)
        val groups = listOf(group1, group2, group3)

        views.forEach {
            it.visibility = View.INVISIBLE
        }

        for ((i, elem) in groups.withIndex()) {
            appear(elem, 500*(i+1).toLong())
        }

        isValid(checkboxes, checked, nextButtonActive, nextButtonInactive)

        nextButtonActive.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun appear(viewList: List<View>, delayTime: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            for (elem in viewList) {
                elem.apply {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@UserPreferencesActivity,
                            R.anim.anim_element_fade_in
                        )
                    )
                }
            }
        }, delayTime)
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
                                    this@UserPreferencesActivity,
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
                                    this@UserPreferencesActivity,
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
}