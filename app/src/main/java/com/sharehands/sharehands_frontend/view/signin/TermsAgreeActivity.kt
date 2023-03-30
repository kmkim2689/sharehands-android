package com.sharehands.sharehands_frontend.view.signin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
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

        val titleText1 = binding.tvAgreeSubtitle
        val titleText2 = binding.tvAgreeTitle
        val titleText3 = binding.tvCheckInfo
        val checkbox1 = binding.checkboxTerm1Agree
        val checkbox2 = binding.checkboxTerm2Agree
        val checkbox3 = binding.checkboxTerm3Agree
        val checkbox4 = binding.checkboxTerm4Agree
        val term1 = binding.tvTerm1Agree
        val term2 = binding.tvTerm2Agree
        val term3 = binding.tvTerm3Agree
        val term4 = binding.tvTerm4Agree


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

        Handler(Looper.getMainLooper()).postDelayed({
            titleText1.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            titleText2.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            titleText3.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            checkbox1.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            term1.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            checkbox2.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            term2.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
        }, 1500)

        Handler(Looper.getMainLooper()).postDelayed({
            checkbox3.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            term3.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
        }, 2000)

        Handler(Looper.getMainLooper()).postDelayed({
            checkbox4.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
            term4.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_element_fade_in))
            }
        }, 2500)

        checkbox1.setOnClickListener {
            if (checkbox1.isChecked) {
                it.startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_checkbox))
            } else {
                it.clearAnimation()
            }
        }

        checkbox2.setOnClickListener {
            if (checkbox2.isChecked) {
                it.startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_checkbox))
            } else {
                it.clearAnimation()
            }
        }

        checkbox3.setOnClickListener {
            if (checkbox3.isChecked) {
                it.startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_checkbox))
            } else {
                it.clearAnimation()
            }
        }

        checkbox4.setOnClickListener {
            if (checkbox4.isChecked) {
                it.startAnimation(AnimationUtils.loadAnimation(this@TermsAgreeActivity, R.anim.anim_checkbox))
            } else {
                it.clearAnimation()
            }
        }
    }
}