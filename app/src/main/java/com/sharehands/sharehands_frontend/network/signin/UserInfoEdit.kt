package com.sharehands.sharehands_frontend.network.signin

import com.google.gson.annotations.SerializedName

data class UserInfoEdit(
    @SerializedName ("nickname") val nickname: String?,
    @SerializedName ("tel") val tel: String?,
    @SerializedName ("location") val location: String?,
    @SerializedName ("interests") val interests: List<String>?
)
/*
"{
        ""nickname : ""test2@test.com""
        ""cellphone: ""010-1234-5678""
        ""location: ""노원구""
        ""interest"": [교육, 의료]
}"*/