package com.pantaubersama.app.data.model.tps

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "c1_form")
data class C1Form(
    @SerializedName("hitung_real_count_id")
    @Expose
    val tpsId: String,
    @SerializedName("form_c1_type")
    @Expose
    val formC1Type: String,
    @SerializedName("a3_laki_laki")
    @Expose
    val a3L: Int,
    @SerializedName("a3_perempuan")
    @Expose
    val a3P: Int,
    @SerializedName("a4_laki_laki")
    @Expose
    val a4L: Int,
    @SerializedName("a4_perempuan")
    @Expose
    val a4P: Int,
    @SerializedName("a_dpk_laki_laki")
    @Expose
    val aDpkL: Int,
    @SerializedName("a_dpk_perempuan")
    @Expose
    val aDpkP: Int,
    @SerializedName("c7_dpt_laki_laki")
    @Expose
    val c7DpkL: Int,
    @SerializedName("c7_dpt_perempuan")
    @Expose
    val c7DpkP: Int,
    @SerializedName("c7_dptb_laki_laki")
    @Expose
    val c7DptBL: Int,
    @SerializedName("c7_dptb_perempuan")
    @Expose
    val c7DptBP: Int,
    @SerializedName("c7_dpk_laki_laki")
    @Expose
    val c7DptL: Int,
    @SerializedName("c7_dpk_perempuan")
    @Expose
    val c7DptP: Int,
    @SerializedName("disabilitas_hak_pilih_laki_laki")
    @Expose
    val disabilitasHakPilihL: Int,
    @SerializedName("disabilitas_hak_pilih_perempuan")
    @Expose
    val disabilitasHakPilihP: Int,
    @SerializedName("disabilitas_terdaftar_laki_laki")
    @Expose
    val disabilitasTerdaftarL: Int,
    @SerializedName("disabilitas_terdaftar_perempuan")
    @Expose
    val disabilitasTerdaftarP: Int,
    @SerializedName("surat_digunakan")
    @Expose
    val suratDigunakan: Int,
    @SerializedName("surat_dikembalikan")
    @Expose
    val suratDikembalikan: Int,
    @SerializedName("surat_tidak_digunakan")
    @Expose
    val suratTidakDigunakan: Int

)