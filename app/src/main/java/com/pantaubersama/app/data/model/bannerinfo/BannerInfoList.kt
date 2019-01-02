package com.pantaubersama.app.data.model.bannerinfo

import com.google.gson.annotations.SerializedName

data class BannerInfoList(
    @SerializedName("banner_infos") var bannerInfoList: MutableList<BannerInfo>
)