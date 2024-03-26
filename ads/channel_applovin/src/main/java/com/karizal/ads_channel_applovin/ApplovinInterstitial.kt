package com.karizal.ads_channel_applovin

import android.app.Activity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.InterstitialContract


class ApplovinInterstitial(private val data: ApplovinData) : InterstitialContract {
    override val name: String = AdsBaseConst.applovin
    override var isDebug: Boolean = false
    override var activity: Activity? = null
    override var onInitializeOK: (name: String) -> Unit = {}
    override var onInitializeError: (name: String) -> Unit = {}
    private var interstitial: MaxInterstitialAd? = null
    private var onHide: () -> Unit = {}
    private var onFailure: (Activity) -> Unit = {}

    private val listener = object : MaxAdListener {
        override fun onAdLoaded(ad: MaxAd) {
            onInitializeOK.invoke(name)
        }

        override fun onAdDisplayed(ad: MaxAd) {

        }

        override fun onAdHidden(ad: MaxAd) {
            interstitial?.destroy()
            onHide.invoke()
            activity?.let { initialize(it, isDebug, onInitializeOK, onInitializeError) }
        }

        override fun onAdClicked(ad: MaxAd) {

        }

        override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
            onInitializeError.invoke(name)
        }

        override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
            activity?.let { onFailure.invoke(it) }
        }
    }

    override fun initialize(
        activity: Activity,
        isDebug: Boolean,
        onInitializeOK: (name: String) -> Unit,
        onInitializeError: (name: String) -> Unit
    ) {
        super.initialize(activity, isDebug, onInitializeOK, onInitializeError)
        data.interstitial_id ?: return onInitializeError.invoke(name)
        interstitial = MaxInterstitialAd(data.interstitial_id, activity)
        interstitial?.setListener(listener)

        interstitial?.loadAd()
    }

    override fun show(
        activity: Activity,
        possibleToShow: (channel: String) -> Boolean,
        onHide: () -> Unit,
        onFailure: (activity: Activity) -> Unit
    ) {
        if (possibleToShow.invoke(name).not()) {
            return onFailure.invoke(activity)
        }

        this.onHide = onHide
        this.onFailure = onFailure

        if (interstitial?.isReady == true) {
            interstitial?.showAd()
        } else {
            onFailure.invoke(activity)
        }
    }
}