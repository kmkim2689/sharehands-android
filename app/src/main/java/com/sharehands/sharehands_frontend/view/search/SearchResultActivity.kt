package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySearchResultBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import com.sharehands.sharehands_frontend.view.signin.SocialLoginActivity
import com.sharehands.sharehands_frontend.viewmodel.search.SearchKeywordViewModel

class SearchResultActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var viewModel: SearchKeywordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        viewModel = ViewModelProvider(this).get(SearchKeywordViewModel::class.java)
        binding.lifecycleOwner = this

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        if (token == "null") {
            showSnackbar("서비스 이용을 위하여 로그인하세요.")
        } else {
            var resultKeyword = intent.getStringExtra("keyword")

            if (resultKeyword != null && resultKeyword.length >= 2) {
                binding.editSearch.setText("$resultKeyword")
                viewModel.searchKeyword(token, resultKeyword)
            }
        }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun showSnackbar(text: String) {
        val snackbar = Snackbar.make(binding.coordinatorLayout, text, Snackbar.LENGTH_LONG)
            .setAction("로그인") {
                val intent = Intent(this, SocialLoginActivity::class.java)
                startActivity(intent)
                MainActivity().finish()
            }
        snackbar.show()
    }
}