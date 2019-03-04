package com.pantaubersama.app.data.model.wordstadium

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Challenge(
        @SerializedName("bidang_kajian")
        var bidangKajian: String?,
        var pernyataan: String?,
        var link: String? = null,
        var date: String?,
        var time: String?,
        @SerializedName("saldo_waktu")
        var saldoWaktu: Int,
        var invitationId: String? = null,
        var screenName: String? = null,
        var name: String? = null,
        var avatar: String? = null
) : Serializable