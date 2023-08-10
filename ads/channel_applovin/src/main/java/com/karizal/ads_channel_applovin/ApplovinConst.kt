package com.karizal.ads_channel_applovin

import android.app.Application
import com.applovin.sdk.AppLovinSdk


object ApplovinConst {
    fun init(application: Application, data: ApplovinData) {
        if (!AppLovinSdk.getInstance(application).isInitialized) {
            AppLovinSdk.getInstance(application).mediationProvider = data.mediation_provider
            AppLovinSdk.initializeSdk(
                application
            ){

            }
        }
    }
}