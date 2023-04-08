package com.sharehands.sharehands_frontend.network.signin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "b03a4ceae5d55bdc92ecfbe00ddaf2c1")
    }
}