package com.sharehands.sharehands_frontend.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivitySearchResultBinding

class SearchResultActivity: AppCompatActivity() {
    lateinit var binding: ActivitySearchResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
    }
}