package com.pantaubersama.app.data.model.tps

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
    var a3L: Int,
    @SerializedName("a3_perempuan")
    @Expose
    var a3P: Int,
    @SerializedName("a4_laki_laki")
    @Expose
    var a4L: Int,
    @SerializedName("a4_perempuan")
    @Expose
    var a4P: Int,
    @SerializedName("a_dpk_laki_laki")
    @Expose
    var aDpkL: Int,
    @SerializedName("a_dpk_perempuan")
    @Expose
    var aDpkP: Int,
    @SerializedName("c7_dpt_laki_laki")
    @Expose
    var c7DpkL: Int,
    @SerializedName("c7_dpt_perempuan")
    @Expose
    var c7DpkP: Int,
    @SerializedName("c7_dptb_laki_laki")
    @Expose
    var c7DptBL: Int,
    @SerializedName("c7_dptb_perempuan")
    @Expose
    var c7DptBP: Int,
    @SerializedName("c7_dpk_laki_laki")
    @Expose
    var c7DptL: Int,
    @SerializedName("c7_dpk_perempuan")
    @Expose
    var c7DptP: Int,
    @SerializedName("disabilitas_terdaftar_laki_laki")
    @Expose
    var disabilitasTerdaftarL: Int,
    @SerializedName("disabilitas_terdaftar_perempuan")
    @Expose
    var disabilitasTerdaftarP: Int,
    @SerializedName("disabilitas_hak_pilih_laki_laki")
    @Expose
    var disabilitasHakPilihL: Int,
    @SerializedName("disabilitas_hak_pilih_perempuan")
    @Expose
    var disabilitasHakPilihP: Int,
    @SerializedName("surat_dikembalikan")
    @Expose
    var suratDikembalikan: Int,
    @SerializedName("surat_tidak_digunakan")
    @Expose
    var suratTidakDigunakan: Int,
    @SerializedName("surat_digunakan")
    @Expose
    var suratDigunakan: Int

)