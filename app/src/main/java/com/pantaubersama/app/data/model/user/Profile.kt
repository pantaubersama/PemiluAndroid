package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

val EMPTY_INFORMANT = Informant("", null, null, null, null,
        null, null, null, null, null)

val EMPTY_PROFILE = Profile("", "", "Tanpa", "", "",
        false, false, null, 0, null, false,
        null, "", "", "", "",
        Image(null), EMPTY_INFORMANT)

data class ProfileResponse(
        @SerializedName("data") val data: ProfileData
) : Serializable

data class ProfileData(
        @SerializedName("user") val user: Profile,
        @SerializedName("users") val users: MutableList<Profile>
) : Serializable

data class Profile(
        @SerializedName("id") var id: String,
        @SerializedName("email") var email: String,
        @SerializedName("full_name") var name: String,
        @SerializedName("uid") var uid: String,
        @SerializedName("provider") var provider: String,
        @SerializedName("is_admin") var isAdmin: Boolean,
        @SerializedName("is_moderator") var isModerator: Boolean,
        @SerializedName("cluster") var cluster: ClusterItem?,
        @SerializedName("vote_preference") var votePreference: Int,
        @SerializedName("political_party") var politicalParty: String?,
        @SerializedName("verified") var verified: Boolean,
        @SerializedName("username") var username: String?,
        @SerializedName("about") var about: String?,
        @SerializedName("location") var location: String?,
        @SerializedName("education") var education: String?,
        @SerializedName("occupation") var occupation: String?,
        @SerializedName("avatar") var avatar: Image,
        @SerializedName("informant") var informant: Informant
) : Serializable, ItemModel {
    override fun getType(): Int = PantauConstants.ItemModel.TYPE_USER
}

data class Informant(
        @SerializedName("user_id") var userId: String,
        @SerializedName("identity_number") var identityNumber: String?,
        @SerializedName("pob") var pob: String?,
        @SerializedName("dob") var dob: String?,
        @SerializedName("gender") var gender: Int?,
        @SerializedName("gender_str") var genderStr: String?,
        @SerializedName("occupation") var occupation: String?,
        @SerializedName("nationality") var nationality: String?,
        @SerializedName("address") var address: String?,
        @SerializedName("phone_number") var phoneNumber: String?
) : Serializable
