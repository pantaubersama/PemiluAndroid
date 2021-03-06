package com.pantaubersama.app.data.model.tps.realcount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.TPS
import java.io.Serializable

@Entity(
    tableName = "real_count",
    foreignKeys = [ForeignKey(
        entity = TPS::class,
        parentColumns = ["id"],
        childColumns = ["hitung_real_count_id"],
        onDelete = CASCADE)]
)
data class RealCount(
    @PrimaryKey
    var id: String,
    @SerializedName("hitung_real_count_id")
    @Expose
    @ColumnInfo(name = "hitung_real_count_id")
    var hitungRealCountId: String,
    @SerializedName("calculation_type")
    @Expose
    var calculationType: String,
    @SerializedName("candidates")
    @Expose
    var candidates: MutableList<Candidate>,
    @SerializedName("invalid_vote")
    @Expose
    var invalidVote: Long,
    @SerializedName("parties")
    @Expose
    var parties: MutableList<Party>
) : Serializable

data class Candidate(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("total_vote")
    @Expose
    var totalVote: Long,
    @SerializedName("actor_id")
    var actorId: Int? = null,
    @SerializedName("actor_type")
    var actorType: String? = null,
    @SerializedName("percentage")
    var percentage: Double? = null
) : ItemModel, Serializable {
    override fun getType(): Int {
        return 0
    }
}

data class Party(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("total_vote")
    @Expose
    var totalVote: Long,
    @SerializedName("actor_id")
    var actorId: Int? = null,
    @SerializedName("actor_type")
    var actorType: String? = null,
    @SerializedName("percentage")
    var percentage: Double? = null
) : Serializable