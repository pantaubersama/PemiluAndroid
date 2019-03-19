package com.pantaubersama.app.data.model.tps.image

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_doc")
data class ImageDoc(
    @PrimaryKey
    var id: String,
    var tpsId: String,
    var presiden: MutableList<Image>,
    var dpr: MutableList<Image>,
    var dpd: MutableList<Image>,
    var dprdProv: MutableList<Image>,
    var dprdKab: MutableList<Image>,
    var suasanaTps: MutableList<Image>
)