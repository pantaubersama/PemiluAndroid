package com.pantaubersama.app.data.model.cluster

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.user.Image

/**
 * @author edityomurti on 27/12/2018 00:11
 */
data class ClusterItem(
    @SerializedName("id")
    @Expose
    var id: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("is_eligible")
    @Expose
    var isEligible: Boolean? = false,
    @SerializedName("image")
    @Expose
    var image: Image? = null
)