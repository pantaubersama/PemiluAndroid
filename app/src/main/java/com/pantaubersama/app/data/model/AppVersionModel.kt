package com.pantaubersama.app.data.model

import com.google.gson.annotations.SerializedName

data class AppVersionResponse(
    @SerializedName("data") var data: AppVersionData
)

data class AppVersionData(
    @SerializedName("app_version") var appVersion: AppVersion
)

data class AppVersion(
    @SerializedName("id") var id: String,
    @SerializedName("version") var versionName: String,
    @SerializedName("version_code") var versionCode: Int,
    @SerializedName("app_type") var appType: String,
    @SerializedName("force_update") var isForceUpdate: Boolean
)