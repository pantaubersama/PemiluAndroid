package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationResponse<D>(
    @SerializedName("data")
    @Expose
    var data: D
)

data class ProvinceData(
    @SerializedName("provinces")
    @Expose
    var provinces: MutableList<Province>
)

data class RegencyData(
    @SerializedName("regencies")
    @Expose
    var regencies: MutableList<Regency>
)

data class DistrictData(
    @SerializedName("districts")
    @Expose
    var districts: MutableList<District>
)

data class VillageData(
    @SerializedName("villages")
    @Expose
    var villages: MutableList<Village>
)