package com.pantaubersama.app.data.model.tags

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_TAG_ITEM

/**
 * @author edityomurti on 22/03/2019 14:48
 */
data class TagsResponse(
    @SerializedName("data")
    val tagsData: TagsData
)

data class TagsData(
    @SerializedName("tags")
    val tags: MutableList<TagItem>
)

data class TagItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
) : ItemModel {
    override fun getType(): Int = TYPE_TAG_ITEM
}