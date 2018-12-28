package com.pantaubersama.app.data.model.bannerinfo

import com.google.gson.annotations.SerializedName

/**
 * @author edityomurti on 27/12/2018 21:03
 */
data class BannerInfoResponse(
    @SerializedName("data") var bannerInfoList: MutableList<BannerInfo>
)