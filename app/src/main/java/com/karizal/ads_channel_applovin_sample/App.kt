package com.karizal.ads_channel_applovin_sample

import android.app.Application
import com.karizal.ads_channel_applovin.ApplovinConst

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ApplovinConst.init(this, Const.applovinData)
    }
}