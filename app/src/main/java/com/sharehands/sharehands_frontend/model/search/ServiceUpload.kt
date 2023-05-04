package com.sharehands.sharehands_frontend.model.search

import okhttp3.MultipartBody

data class ServiceUpload(
    var category: String?,
    var title: String?,
    var introduce: String?,
    var applyDeadline: String?,
    var area: String?,
    var startDate: String?,
    var endDate: String?,
    var dow: ArrayList<String>?,
    var startTime: String?,
    var endTime: String?,
    var recruitNum: String?,
    var cost: String?,
    var content: String?,
    var tel: String?,
    var email: String?,
    var extra: String?,
    var imageList: ArrayList<MultipartBody.Part>?
)
