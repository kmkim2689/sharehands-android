package com.sharehands.sharehands_frontend.viewmodel.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.view.search.LocationSearchActivity

class ServiceLocationViewModel: ViewModel() {
    var roadNameAddress = MutableLiveData<String>()
    var detailAddress = MutableLiveData<String>()

    fun onDetailChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        detailAddress.value = s.toString()
        Log.d("reverse detail address", "${detailAddress.value}")

    }
}