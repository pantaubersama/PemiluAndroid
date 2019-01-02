package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName
import com.pantaubersama.app.data.model.cluster.ClusterItem

val EMPTY_INFORMANT = Informant("", null, null, null, null,
        null, null, null, null, null)

val EMPTY_PROFILE = Profile("", "", "Tanpa", "Nama", "", "",
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
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("is_admin") val isAdmin: Boolean,
    @SerializedName("is_moderator") val isModerator: Boolean,
    @SerializedName("cluster") val cluster: ClusterItem?,
    @SerializedName("vote_preference") val votePreference: Any?,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("username") val username: String?,
    @SerializedName("about") val about: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("education") val education: String?,
    @SerializedName("occupation") val occupation: String?,
    @SerializedName("avatar") val avatar: Avatar,
    @SerializedName("informant") val informant: Informant
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
