package com.karizal.ads_channel_applovin

import com.karizal.ads_base.data.BasicAdsData

class ApplovinData(
    val key: String,
    val mediation_provider: String,
    val banner_id: String? = null,
    val interstitial_id: String? = null,
    val native_id: String? = null,
    val reward_id: String? = null,
    val app_open_id: String? = null,
) : BasicAdsData(name = "applovin")