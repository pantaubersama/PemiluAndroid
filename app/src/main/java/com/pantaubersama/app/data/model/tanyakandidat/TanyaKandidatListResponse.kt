package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TanyaKandidatListResponse(
    @SerializedName("data")
    @Expose
    var data: TanyaKandidatListData? = null
) : Serializable

class TanyaKandidatResponse(
    @SerializedName("data")
    var data: TanyaKandidatData? = null
)