package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.PantauConstants
import java.io.Serializable

val EMPTY_INFORMANT = Informant("", null, null, null, null,
    null, null, null, null, null)

val EMPTY_PROFILE = Profile("", "", "Anda", "", "",
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
    @SerializedName("id") var id: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("uid") var uid: String? = null,
    @SerializedName("provider") var provider: String? = null,
    @SerializedName("is_admin") var isAdmin: Boolean? = null,
    @SerializedName("is_moderator") var isModerator: Boolean? = false,
    @SerializedName("cluster") var cluster: ClusterItem? = null,
    @SerializedName("vote_preference") var votePreference: Int? = null,
    @SerializedName("political_party") var politicalParty: PoliticalParty? = null,
    @SerializedName("verified") var verified: Boolean? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("about") var about: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("education") var education: String? = null,
    @SerializedName("occupation") var occupation: String? = null,
    @SerializedName("avatar") var avatar: Image? = null,
    @SerializedName("informant") var informant: Informant? = null
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
