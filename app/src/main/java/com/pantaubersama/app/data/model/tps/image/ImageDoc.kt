package com.pantaubersama.app.data.model.tps.image

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_doc")
data class ImageDoc(
    @PrimaryKey
    var id: String,
    var tpsId: String,
    var presiden: MutableList<ImageLocalModel>,
    var dpr: MutableList<ImageLocalModel>,
    var dpd: MutableList<ImageLocalModel>,
    var dprdProv: MutableList<ImageLocalModel>,
    var dprdKab: MutableList<ImageLocalModel>,
    var suasanaTps: MutableList<ImageLocalModel>
)