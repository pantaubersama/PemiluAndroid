package com.pantaubersama.app.data.model.tps

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Dapil(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("nama")
    @Expose
    val nama: String,
    @SerializedName("tingkat")
    @Expose
    val tingkat: String,
    @SerializedName("jumlahPenduduk")
    @Expose
    val jumlahPenduduk: String,
    @SerializedName("alokasiKursi")
    @Expose
    val alokasiKursi: Int,
    @SerializedName("idWilayah")
    @Expose
    val idWilayah: Int,
    @SerializedName("totalAlokasiKursi")
    @Expose
    val totalAlokasiKursi: Int,
    @SerializedName("idVersi")
    @Expose
    val idVersi: Int,
    @SerializedName("noDapil")
    @Expose
    val noDapil: Int,
    @SerializedName("statusCoterminous")
    @Expose
    val statusCoterminous: Boolean,
    @SerializedName("idPro")
    @Expose
    val idPro: Int,
    @SerializedName("parent")
    @Expose
    val parent: Int,
    @SerializedName("alokasiSisaKursi")
    @Expose
    val alokasiSisaKursi: Int,
    @SerializedName("sisaPenduduk")
    @Expose
    val sisaPenduduk: Int,
    @SerializedName("peringkatPenduduk")
    @Expose
    val peringkatPenduduk: Int,
    @SerializedName("stdDev")
    @Expose
    val stdDev: Int,
    @SerializedName("mean")
    @Expose
    val mean: Int,
    @SerializedName("dapilOwner")
    @Expose
    val dapilOwner: Int,
    @SerializedName("maxAlokasiKursi")
    @Expose
    val maxAlokasiKursi: Int,
    @SerializedName("minAlokasiKursi")
    @Expose
    val minAlokasiKursi: Int
)