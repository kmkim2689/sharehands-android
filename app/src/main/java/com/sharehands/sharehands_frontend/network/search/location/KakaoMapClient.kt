package com.sharehands.sharehands_frontend.network.search.location

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object KakaoMapClient {
    private const val BASE_URL = "dapi.kakao.com"
    private const val API_KEY = "KakaoAK 06fd1b4ec34194550bd89d358e0840b1"

    private val mapRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

interface KakaoMapApiService {
    @GET ("/v2/local/search/address.json")
    fun getKakaoAddress(
        @Header("Authorization") key: String,
        @Query("query") address: String
    ): Call<KakaoMapData>
}

