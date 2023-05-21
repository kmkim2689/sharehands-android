package com.sharehands.sharehands_frontend.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.home.HomeData
import com.sharehands.sharehands_frontend.network.home.PopularItem
import com.sharehands.sharehands_frontend.network.home.RankingItem
import com.sharehands.sharehands_frontend.network.home.SuggestedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var _homeData = MutableLiveData<HomeData?>()
    val homeData: LiveData<HomeData?>
        get() = _homeData

    private var _nickname = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickname

    private var _profileUrl = MutableLiveData<String>()
    val profileUrl: LiveData<String>
        get() = _profileUrl

    private var _invitationNum = MutableLiveData<String>()
    val invitationNum: LiveData<String>
        get() = _invitationNum

    private var _suggestedItems = MutableLiveData<List<SuggestedItem>>()
    val suggestedItems: LiveData<List<SuggestedItem>>
        get() = _suggestedItems

    private var _userRankings = MutableLiveData<List<RankingItem>>()
    val userRankings: LiveData<List<RankingItem>>
        get() = _userRankings

    private var _serviceRankings = MutableLiveData<List<PopularItem>>()
    val serviceRankings: LiveData<List<PopularItem>>
        get() = _serviceRankings


    fun getMainPage(token: String) {
        RetrofitClient.createRetorfitClient().getMainPage(token)
            .enqueue(object : Callback<HomeData> {
                override fun onResponse(call: Call<HomeData>, response: Response<HomeData>) {
                    if (response.code() == 200) {
                        Log.d("메인 화면 데이터 불러오기 성공", "${response.body()}")

                        val body = response.body()
                        if (body != null) {
                            _homeData.value = body
                            _nickname.value = body.nickname
                            _invitationNum.value = body.invitation.toString()
                            _profileUrl.value = body.profileUrl
                            _suggestedItems.value = body.suggestion
                            _userRankings.value = body.rankings
                            _serviceRankings.value = body.popular
                        }
                    } else {
                        Log.d("메인 화면 데이터 불러오기 실패", "${response.code()}")
                    }
                }

                override fun onFailure(call: Call<HomeData>, t: Throwable) {
                    Log.d("메인 화면 데이터 불러오기 실패", "${t.message}")
                }

            })

    }

}