package com.sharehands.sharehands_frontend.model.search

import okhttp3.MultipartBody

data class ServiceUpload(
    val category: String,
    val name: String,
    val intro: String,
    val due: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val weekday: ArrayList<String>,
    val startTime: String,
    val endTime: String,
    val maxNum: String,
    val expense: String,
    val detailDesc: String,
    val tel: String?,
    val email: String?,
    val contactEtc: String?,
    val imageList: ArrayList<MultipartBody.Part>
)
