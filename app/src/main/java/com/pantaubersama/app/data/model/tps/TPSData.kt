package com.pantaubersama.app.data.model.tps

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.user.Profile

@Entity(tableName = "tps")
data class TPSData(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("tps")
    @Expose
    var tps: Int,
    @SerializedName("province")
    @Expose
    var province: Province,
    @SerializedName("regency")
    @Expose
    var regency: Regency,
    @SerializedName("district")
    @Expose
    var district: District,
    @SerializedName("village")
    @Expose
    var village: Village,
    @SerializedName("latitude")
    @Expose
    var latitude: Float,
    @SerializedName("longitude")
    @Expose
    var longitude: Float,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("created_at_in_word")
    @Expose
    var createdAtInWord: CreatedAtInWord
) : ItemModel {
    override fun getType(): Int {
        return 14
    }
}