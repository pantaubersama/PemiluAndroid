package com.pantaubersama.app.data.model.meta

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.meta.pages.Pages
import java.io.Serializable

data class Meta(
    @SerializedName("pages")
    @Expose
    var pages: Pages? = null
) : Serializable