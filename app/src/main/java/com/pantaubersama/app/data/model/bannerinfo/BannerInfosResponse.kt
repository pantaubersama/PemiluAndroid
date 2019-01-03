package com.pantaubersama.app.data.model.bannerinfo

import com.google.gson.annotations.SerializedName

/**
 * @author edityomurti on 27/12/2018 21:03
 */
data class BannerInfosResponse(
    @SerializedName("data") var data: BannerInfoList
)

data class BannerInfoResponse(
    @SerializedName("data") var data: BannerInfoData
)

data class BannerInfoData(
    @SerializedName("banner_info") var bannerInfo: BannerInfo
)