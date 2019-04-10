package com.pantaubersama.app.data.model.tps.c1

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "c1_form")
data class C1Form(
    @PrimaryKey
    var id: String,
    @SerializedName("hitung_real_count_id")
    @Expose
    var tpsId: String,
    @SerializedName("form_c1_type")
    @Expose
    var formC1Type: String,
    @SerializedName("a3_laki_laki")
    @Expose
    var a3L: Long,
    @SerializedName("a3_perempuan")
    @Expose
    var a3P: Long,
    @SerializedName("a4_laki_laki")
    @Expose
    var a4L: Long,
    @SerializedName("a4_perempuan")
    @Expose
    var a4P: Long,
    @SerializedName("a_dpk_laki_laki")
    @Expose
    var aDpkL: Long,
    @SerializedName("a_dpk_perempuan")
    @Expose
    var aDpkP: Long,
    @SerializedName("c7_dpt_laki_laki")
    @Expose
    var c7DpkL: Long,
    @SerializedName("c7_dpt_perempuan")
    @Expose
    var c7DpkP: Long,
    @SerializedName("c7_dptb_laki_laki")
    @Expose
    var c7DptBL: Long,
    @SerializedName("c7_dptb_perempuan")
    @Expose
    var c7DptBP: Long,
    @SerializedName("c7_dpk_laki_laki")
    @Expose
    var c7DptL: Long,
    @SerializedName("c7_dpk_perempuan")
    @Expose
    var c7DptP: Long,
    @SerializedName("disabilitas_terdaftar_laki_laki")
    @Expose
    var disabilitasTerdaftarL: Long,
    @SerializedName("disabilitas_terdaftar_perempuan")
    @Expose
    var disabilitasTerdaftarP: Long,
    @SerializedName("disabilitas_hak_pilih_laki_laki")
    @Expose
    var disabilitasHakPilihL: Long,
    @SerializedName("disabilitas_hak_pilih_perempuan")
    @Expose
    var disabilitasHakPilihP: Long,
    @SerializedName("surat_dikembalikan")
    @Expose
    var suratDikembalikan: Long,
    @SerializedName("surat_tidak_digunakan")
    @Expose
    var suratTidakDigunakan: Long,
    @SerializedName("surat_digunakan")
    @Expose
    var suratDigunakan: Long

)