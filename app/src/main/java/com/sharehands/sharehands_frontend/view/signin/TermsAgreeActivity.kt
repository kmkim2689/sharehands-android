package com.sharehands.sharehands_frontend.view.signin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityTermsAgreeBinding

class TermsAgreeActivity: AppCompatActivity() {
    lateinit var binding: ActivityTermsAgreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_agree)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_agree)

        binding.btnBack.setOnClickListener {
            finish()
        }

        // UI 작업
        val titleText1 = binding.tvAgreeSubtitle
        val titleText2 = binding.tvAgreeTitle
        val titleText3 = binding.tvCheckInfo
        val checkbox1 = binding.checkboxTerm1Agree
        val checkbox2 = binding.checkboxTerm2Agree
        val checkbox3 = binding.checkboxTerm3Agree
        val checkbox4 = binding.checkboxTerm4Agree
        val checkbox5 = binding.checkboxEntireAgree
        val term1 = binding.tvTerm1Agree
        val term2 = binding.tvTerm2Agree
        val term3 = binding.tvTerm3Agree
        val term4 = binding.tvTerm4Agree
        val termsEntire = binding.layoutEntireAgree
        val nextBtnLayout = binding.layoutBtnNext
        val nextBtnActive = binding.btnNextActive
        val nextBtnInactive = binding.btnNextInactive
        val nextBtnList: List<TextView> = listOf(nextBtnActive, nextBtnInactive)
        val checkboxList: List<CheckBox> = listOf(checkbox1, checkbox2, checkbox3, checkbox4, checkbox5)


        titleText1.visibility = View.INVISIBLE
        titleText2.visibility = View.INVISIBLE
        titleText3.visibility = View.INVISIBLE
        checkbox1.visibility = View.INVISIBLE
        term1.visibility = View.INVISIBLE
        checkbox2.visibility = View.INVISIBLE
        term2.visibility = View.INVISIBLE
        checkbox3.visibility = View.INVISIBLE
        term3.visibility = View.INVISIBLE
        checkbox4.visibility = View.INVISIBLE
        term4.visibility = View.INVISIBLE
        termsEntire.visibility = View.INVISIBLE
        nextBtnLayout.visibility = View.INVISIBLE
        nextBtnActive.visibility = View.INVISIBLE
        nextBtnInactive.visibility = View.INVISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            appear(titleText1)
            appear(titleText2)
            appear(titleText3)
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(checkbox1)
            appear(term1)
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(checkbox2)
            appear(term2)
        }, 1500)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(checkbox3)
            appear(term3)
        }, 2000)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(checkbox4)
            appear(term4)
        }, 2500)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(termsEntire)
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            appear(nextBtnLayout)
            appear(nextBtnInactive)
        }, 3500)

        checkbox1.setOnClickListener {
            checkTerm(checkbox1, it, checkboxList, nextBtnList)
        }

        checkbox2.setOnClickListener {
            checkTerm(checkbox2, it, checkboxList, nextBtnList)
        }

        checkbox3.setOnClickListener {
            checkTerm(checkbox3, it, checkboxList, nextBtnList)
        }

        checkbox4.setOnClickListener {
            checkTerm(checkbox4, it, checkboxList, nextBtnList)
        }

        checkbox5.setOnClickListener {
            checkEntireTerms(checkboxList, it, nextBtnList)
        }

        binding.btnNextInactive.setOnClickListener {
            binding.tvWarning.visibility = View.VISIBLE
            binding.tvWarning.startAnimation(
                AnimationUtils.loadAnimation(
                    this@TermsAgreeActivity,
                    R.anim.anim_element_fade_in
                )
            )
        }

        binding.btnNextActive.setOnClickListener {
            val intent = Intent(this, PermissionsAgreeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun appear(titleText1: View) {
        titleText1.apply {
            visibility = View.VISIBLE
            startAnimation(
                AnimationUtils.loadAnimation(
                    this@TermsAgreeActivity,
                    R.anim.anim_element_fade_in
                )
            )
        }
    }

    private fun checkEntireTerms(checkboxList: List<CheckBox>, it: View, nextBtnList: List<TextView>) {
        if (checkboxList[4].isChecked) {
            checkboxList[0].isChecked = true
            checkboxList[1].isChecked = true
            checkboxList[2].isChecked = true
            checkboxList[3].isChecked = true
            checkTerm(checkboxList[0], it, checkboxList, nextBtnList)
            checkTerm(checkboxList[1], it, checkboxList, nextBtnList)
            checkTerm(checkboxList[2], it, checkboxList, nextBtnList)
            checkTerm(checkboxList[3], it, checkboxList, nextBtnList)
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    this@TermsAgreeActivity,
                    R.anim.anim_checkbox
                )
            )
        } else {
            checkboxList[0].isChecked = false
            checkboxList[1].isChecked = false
            checkboxList[2].isChecked = false
            checkboxList[3].isChecked = false
            it.clearAnimation()
        }
    }

    private fun checkTerm(checkbox: CheckBox, it: View, checkboxList: List<CheckBox>, nextBtnList: List<TextView>) {
        if (checkbox.isChecked) {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    this@TermsAgreeActivity,
                    R.anim.anim_checkbox
                )
            )
        } else {
            it.clearAnimation()
        }

        // 4개가 모두 체크되면 전체 약관도 체크. 안그러면 체크 해제
        checkboxList[4].isChecked = checkboxList[0].isChecked && checkboxList[1].isChecked && checkboxList[2].isChecked && checkboxList[3].isChecked

        // 다음 버튼 활성화
        if (checkboxList[0].isChecked && checkboxList[1].isChecked) {
            nextBtnList[0].apply {
                if (visibility == View.INVISIBLE) {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@TermsAgreeActivity,
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