package com.pantaubersama.app.data.model.rekapitulasi

import com.google.gson.annotations.SerializedName

data class TotalParticipantResponse(
    @SerializedName("data")
    var data: TotalParticipantData
)