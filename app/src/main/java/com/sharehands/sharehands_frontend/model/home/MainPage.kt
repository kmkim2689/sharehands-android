package com.sharehands.sharehands_frontend.model.home

data class MainPage(
    val nickname: String,
    val profileUrl: String,
    val invitation: Int,
    val suggestion: List<Suggestion>,
    val popular: List<Popular>,
    val ranking: List<UserRanking>
)

data class Suggestion(
    val thumbnailUrl: String,
    val title: String,
    // 예) "2022. 01. 01. ~ 2022. 01. 10."
    val period: String,
    // 예) [월요일, 화요일]
    val weekday: List<String>,
    // 예) "15:00 ~ 16:00"
    val time: String,
    val location: String,
    val serviceId: Int
)

data class Popular(
    // 예) 1
    val ranking: Int,
    val title: String,
    val views: Int,
    val likes: Int,
    val scraps: Int,
    val serviceId: Int
)

data class UserRanking(
    // 예) 1
    val ranking: Int,
    val nickname: String,
    val level: Int,
    val count: Int,
    val userId: Int
)