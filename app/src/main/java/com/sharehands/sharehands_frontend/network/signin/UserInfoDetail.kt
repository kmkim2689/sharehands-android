package com.sharehands.sharehands_frontend.network.signin

import com.google.gson.annotations.SerializedName

data class UserInfoDetail(
    @SerializedName ("name") val name: String,
    @SerializedName ("nickname") val nickname: String,
    @SerializedName ("cellphone") val cellphone: String,
    @SerializedName ("DOB") val dayOfBirthday: String,
    @SerializedName ("location") val location: String,
    @SerializedName ("interest") val interest: List<String>
)

/*
* "{
        ""name"" : ""someName"",
        ""nickname : ""test2@test.com""
        ""cellphone: ""010-1234-5678""
        ""DOB: ""19990113""
        ""location"": ""노원구""
        ""interest"": [교육, 의료]
}"*/
