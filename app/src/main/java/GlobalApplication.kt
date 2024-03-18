package com.daehankang.myandroidapplication

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,"e3ec750735f5b739a5141adb46ef51ee")
    }
}