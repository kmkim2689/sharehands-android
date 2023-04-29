package com.sharehands.sharehands_frontend.view.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.mypage.MyPageMenuRVAdapter
import com.sharehands.sharehands_frontend.adapter.mypage.MyPageServiceRVAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentMyPageBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myPageMenuRVAdapter = MyPageMenuRVAdapter(requireContext() as MainActivity)
//        binding.rvEtcService.adapter = myPageMenuRVAdapter
//        binding.rvEtcService.layoutManager = LinearLayoutManager(requireContext() as MainActivity)
//
//        val myPageServiceRVAdapter = MyPageServiceRVAdapter(requireContext() as MainActivity)
//        binding.rvMgtService.adapter = myPageServiceRVAdapter
//        binding.rvMgtService.layoutManager = LinearLayoutManager(requireContext() as MainActivity, LinearLayoutManager.HORIZONTAL, false)

        val sp = SharedPreferencesManager.getInstance(requireContext())
        val token = sp.getString("token", "null")
        if (token != "null") {
            binding.layoutStatusLoggedIn.visibility = View.VISIBLE
            binding.layoutStatusLoggedOut.visibility = View.INVISIBLE
            binding.tvUserLogin.visibility = View.INVISIBLE
            binding.tvUserNickname.visibility = View.VISIBLE
            binding.ivCertBadge.visibility = View.INVISIBLE
            binding.layoutUserLevel.visibility = View.VISIBLE
//            binding.rvMgtService.visibility = View.VISIBLE
            binding.layoutMgtService.visibility = View.VISIBLE
        } else {
            binding.layoutStatusLoggedIn.visibility = View.INVISIBLE
            binding.layoutStatusLoggedOut.visibility = View.VISIBLE
            binding.tvUserLogin.visibility = View.VISIBLE
            binding.tvUserNickname.visibility = View.INVISIBLE
            binding.ivCertBadge.visibility = View.INVISIBLE
            binding.layoutUserLevel.visibility = View.INVISIBLE
//            binding.rvMgtService.visibility = View.INVISIBLE
            binding.layoutMgtService.visibility = View.INVISIBLE
        }

        binding.btnRecruit.setOnClickListener {
            val recruitIntent = Intent(requireContext(), RecruitedServiceActivity::class.java)
            startActivity(recruitIntent)
        }

        binding.btnApplied.setOnClickListener {
            val appliedIntent = Intent(requireContext(), AppliedServiceActivity::class.java)
            startActivity(appliedIntent)
        }

        binding.btnParticipated.setOnClickListener {
            val participatedIntent = Intent(requireContext(), ParticipatedServiceActivity::class.java)
            startActivity(participatedIntent)
        }

        binding.btnScraped.setOnClickListener {
            val scrapedIntent = Intent(requireContext(), ScrapedServiceActivity::class.java)
            startActivity(scrapedIntent)
        }

        binding.btnSuggested.setOnClickListener {
            val suggestedIntent = Intent(requireContext(), SuggestedServiceActivity::class.java)
            startActivity(suggestedIntent)
        }

        binding.btnNotice.setOnClickListener {
            val noticeIntent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(noticeIntent)
        }

        binding.btnAppInfo.setOnClickListener {
            val appInfoIntent = Intent(requireContext(), AppInfoActivity::class.java)
            startActivity(appInfoIntent)
        }

        binding.btnFaq.setOnClickListener {
            val faqIntent = Intent(requireContext(), FAQActivity::class.java)
            startActivity(faqIntent)
        }

        binding.btnContact.setOnClickListener {
            val contactIntent = Intent(requireContext(), ContactActivity::class.java)
            startActivity(contactIntent)
        }

        binding.tvLogout.setOnClickListener {
            sp.deleteStringByKey("token")
            sp.deleteStringByKey("email")
            val result = sp.getString("token", "null")
            Log.d("result", result)
            if (result == "null") {
                val socialLoginIntent = Intent(requireContext(), SocialLoginActivity::class.java)
                startActivity(socialLoginIntent)
                activity?.finish()
            }
        }

        binding.tvUnregister.setOnClickListener {
            val withdrawIntent = Intent(requireContext(), WithdrawActivity::class.java)
            startActivity(withdrawIntent)
        }

    }

}