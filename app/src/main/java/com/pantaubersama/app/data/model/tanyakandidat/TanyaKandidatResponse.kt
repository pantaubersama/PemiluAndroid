package com.pantaubersama.app.data.model.tanyakandidat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TanyaKandidatResponse(
    @SerializedName("data")
    @Expose
    var data: TanyaKandidatData? = null
) : Serializable