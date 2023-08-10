package com.karizal.ads_channel_applovin

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.view.allViews
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkUtils
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.BannerContract


class ApplovinBanner(
    private val data: ApplovinData
) : BannerContract {
    override val name: String = AdsBaseConst.applovin
    override var isDebug: Boolean = false
    override var activity: Activity? = null

    override fun initialize(activity: Activity, isDebug: Boolean) {
        super.initialize(activity, isDebug)
        if (!AppLovinSdk.getInstance(activity).isInitialized) {
            AppLovinSdk.getInstance(activity).mediationProvider = data.mediation_provider
            AppLovinSdk.initializeSdk(
                activity
            ) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.initializeSdk")
            }
        }
    }

    override fun fetch(
        container: ViewGroup,
        preparing: () -> Unit,
        possibleToLoad: () -> Boolean,
        onSuccessLoaded: (channel: String) -> Unit,
        onFailedLoaded: () -> Unit
    ) {
        activity ?: return
        prepareContainerView(container)
        preparing.invoke()

        val banner = MaxAdView(data.banner_id, activity)

        val heightDp = MaxAdFormat.BANNER.getAdaptiveSize(activity).height
        val heightPx = AppLovinSdkUtils.dpToPx(activity, heightDp)
        banner.setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                val view = container.allViews.first()
                view.layoutParams = FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    AppLovinSdkUtils.dpToPx(activity, ad?.size?.height ?: heightDp),
                )
                onSuccessLoaded.invoke(name)
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdLoaded")
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdDisplayed")
            }

            override fun onAdHidden(ad: MaxAd?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdHidden")
            }

            override fun onAdClicked(ad: MaxAd?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdClicked")
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                if (possibleToLoad.invoke()) {
                    hideView(container)
                    onFailedLoaded.invoke()
                }
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdLoadFailed")
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdDisplayFailed")
            }

            override fun onAdExpanded(ad: MaxAd?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdExpanded")
            }

            override fun onAdCollapsed(ad: MaxAd?) {
                Log.i(this@ApplovinBanner.getClassName(), "Applovin.banner.onAdCollapsed")
            }

        })

        banner.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            heightPx,
        )

        banner.setExtraParameter("adaptive_banner", "true")

        if (possibleToLoad.invoke()) {
            container.removeAllViewsInLayout()
            container.addView(banner)
            banner.loadAd()
        }
    }
}