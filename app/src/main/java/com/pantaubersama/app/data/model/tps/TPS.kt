package com.pantaubersama.app.data.model.tps

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.user.Profile
import java.io.Serializable

@Entity(tableName = "tps")
data class TPS(
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
    var village: Village? = null,
    @SerializedName("latitude")
    @Expose
    var latitude: Double,
    @SerializedName("longitude")
    @Expose
    var longitude: Double,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("created_at_in_word")
    @Expose
    var createdAtInWord: CreatedAtInWord,
    @SerializedName("user")
    @Expose
    var user: Profile
//    @SerializedName("logs")
//    @Expose
//    var logs: Logs
) : ItemModel, Serializable {
    override fun getType(): Int {
        return 14
    }
}