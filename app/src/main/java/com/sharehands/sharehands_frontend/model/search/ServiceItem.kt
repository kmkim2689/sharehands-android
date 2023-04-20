package com.sharehands.sharehands_frontend.model.search

data class ServiceItem(
    val serviceId: Int,
    val thumbnail: String,
    val userImage: String,
    val userNickname: String,
    val isCertified: Boolean,
    val isApplied: Boolean,
    val title: String,
    val location: String,
    val period: String,
    val maxNum: String,
    val day: String
)
