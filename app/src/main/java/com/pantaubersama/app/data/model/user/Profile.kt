package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.cluster.ClusterItem

val EMPTY_INFORMANT = Informant("", null, null, null, null,
        null, null, null, null, null)

val EMPTY_PROFILE = Profile("", "", "Tanpa", "", "",
        false, false, null, null, false,
        null, "", "", "", "",
        Avatar(null), EMPTY_INFORMANT)

data class ProfileResponse(
    @SerializedName("data") val data: ProfileData
)

data class ProfileData(
    @SerializedName("user") val user: Profile
)

data class Profile(
    @SerializedName("id") var id: String,
    @SerializedName("email") var email: String,
    @SerializedName("full_name") var name: String,
    @SerializedName("uid") var uid: String,
    @SerializedName("provider") var provider: String,
    @SerializedName("is_admin") var isAdmin: Boolean,
    @SerializedName("is_moderator") var isModerator: Boolean,
    @SerializedName("cluster") var cluster: ClusterItem?,
    @SerializedName("vote_preference") var votePreference: Any?,
    @SerializedName("verified") var verified: Boolean,
    @SerializedName("username") var username: String?,
    @SerializedName("about") var about: String?,
    @SerializedName("location") var location: String?,
    @SerializedName("education") var education: String?,
    @SerializedName("occupation") var occupation: String?,
    @SerializedName("avatar") var avatar: Avatar,
    @SerializedName("informant") var informant: Informant
)

data class Informant(
    @SerializedName("user_id") val userId: String,
    @SerializedName("identity_number") val identityNumber: String?,
    @SerializedName("pob") val pob: String?,
    @SerializedName("dob") val dob: String?,
    @SerializedName("gender") val gender: Any?,
    @SerializedName("gender_str") val genderStr: String?,
    @SerializedName("occupation") val occupation: String?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phone_number") val phoneNumber: String?
)
