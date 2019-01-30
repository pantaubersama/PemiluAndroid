package com.pantaubersama.app.ui.profile.verifikasi

import android.content.Context
import android.content.Intent
import com.pantaubersama.app.data.model.user.VerificationStep
import com.pantaubersama.app.ui.profile.verifikasi.finalstep.FinalScreenVerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step0.Step0VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step2.Step2VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiActivity

object VerifikasiNavigator {

    fun start(context: Context, step: VerificationStep) {
        val clazz = when (step) {
            VerificationStep.SUBMIT_KTP_NO -> Step0VerifikasiActivity::class.java
            VerificationStep.SUBMIT_SELFIE -> Step1VerifikasiActivity::class.java
            VerificationStep.SUBMIT_KTP_PHOTO -> Step2VerifikasiActivity::class.java
            VerificationStep.SUBMIT_SIGNATURE -> Step3VerifikasiActivity::class.java
            VerificationStep.FINISHED -> FinalScreenVerifikasiActivity::class.java
        }
        context.startActivity(Intent(context, clazz))
    }
}