package com.pantaubersama.app.data.model.user

import com.google.gson.annotations.SerializedName

data class VerificationResponse(
    @SerializedName("data") val verificationData: VerificationData
)

data class VerificationData(
    @SerializedName("user") val user: UserVerification
)

data class UserVerification(
    @SerializedName("approved") val approved: Boolean,
    @SerializedName("is_verified") val isVerified: Boolean,
    @SerializedName("next_step") val nextStep: Int,
    @SerializedName("step") val step: Int?,
    @SerializedName("user_id") val userId: String
)

enum class VerificationStep {
    SUBMIT_KTP_NO, SUBMIT_SELFIE, SUBMIT_KTP_PHOTO, SUBMIT_SIGNATURE, FINISHED
}