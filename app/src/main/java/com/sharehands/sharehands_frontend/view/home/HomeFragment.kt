package com.sharehands.sharehands_frontend.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.home.PopularRVAdapter
import com.sharehands.sharehands_frontend.adapter.home.SuggestionVPAdapter
import com.sharehands.sharehands_frontend.adapter.home.UserRankingRVAdapter
import com.sharehands.sharehands_frontend.databinding.FragmentHomeBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.mypage.SuggestedServiceActivity
import com.sharehands.sharehands_frontend.view.mypage.UserInfoActivity
import com.sharehands.sharehands_frontend.viewmodel.home.HomeViewModel


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val token =
            SharedPreferencesManager.getInstance(requireContext()).getString("token", "null")

        if (token != "null") {
            viewModel.getMainPage(token)

            viewModel.profileUrl.observe(viewLifecycleOwner) {
                if (viewModel.profileUrl.value != null) {
                    Glide.with(requireContext())
                        .load(viewModel.profileUrl.value.toString())
                        .into(binding.ivUser)
                }
            }

            viewModel.nickname.observe(viewLifecycleOwner) {
                if (viewModel.nickname.value != null) {
                    binding.tvUser.text = viewModel.nickname.value
                }
            }

            viewModel.suggestedItems.observe(viewLifecycleOwner) {
                if (viewModel.suggestedItems.value!!.isNotEmpty()) {
                    val viewPagerSuggestion = binding.viewpagerRecommend
                    val suggestionVPAdapter = SuggestionVPAdapter(
                        requireActivity(),
                        viewModel.suggestedItems.value!!
                    )

                    viewPagerSuggestion.adapter = suggestionVPAdapter

                    val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.recommendMargin)
                    val pagerWidth = resources.getDimensionPixelOffset(R.dimen.recommendWidth)
                    val screenWidth = resources.displayMetrics.widthPixels
                    val offsetPx = screenWidth - pageMarginPx - pagerWidth
                    viewPagerSuggestion.setPageTransformer { page, position ->
                        page.translationX = -page.translationX * position
                    }
                }
            }

            viewModel.userRankings.observe(viewLifecycleOwner) {
                if (viewModel.userRankings.value!!.isNotEmpty()) {
                    val userRecyclerView = binding.rvRankingService
                    val userRankingRVAdapter = UserRankingRVAdapter(
                        context as MainActivity,
                        viewModel.userRankings.value!!
                    )
                    userRecyclerView.adapter = userRankingRVAdapter
                    userRecyclerView.layoutManager = layoutManager
                }
            }

            viewModel.invitationNum.observe(viewLifecycleOwner) {
                if (viewModel.invitationNum.value!!.toInt() == 0) {
                    binding.tvAlarm.visibility = View.INVISIBLE
                }
            }

            viewModel.serviceRankings.observe(viewLifecycleOwner) {
                if (viewModel.serviceRankings.value!!.isNotEmpty()) {
                    val popularRecyclerView = binding.rvHotService
                    val popularRVAdapter =
                        PopularRVAdapter(context as MainActivity, viewModel.serviceRankings.value!!)
                    popularRecyclerView.adapter = popularRVAdapter
                    popularRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
            }

            binding.ivUser.setOnClickListener {
                val infoIntent = Intent(requireContext(), UserInfoActivity::class.java)
                startActivity(infoIntent)
            }

            binding.ivAlert.setOnClickListener {
                val suggestionIntent =
                    Intent(requireContext(), SuggestedServiceActivity::class.java)
                startActivity(suggestionIntent)
            }

        }


    }
}


