package com.sharehands.sharehands_frontend.model.search

import okhttp3.MultipartBody

data class ServiceUpload(
    var category: String?,
    var name: String?,
    var intro: String?,
    var due: String?,
    var location: String?,
    var startDate: String?,
    var endDate: String?,
    var weekday: ArrayList<String>?,
    var startTime: String?,
    var endTime: String?,
    var maxNum: String?,
    var expense: String?,
    var detailDesc: String?,
    var tel: String?,
    var email: String?,
    var contactEtc: String?,
    var imageList: ArrayList<MultipartBody.Part>?
)
