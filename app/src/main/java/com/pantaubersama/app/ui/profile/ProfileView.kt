package com.pantaubersama.app.ui.profile

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.user.VerificationStep
import com.pantaubersama.app.utils.State

interface ProfileView : BaseView {
    fun showProfile(profile: Profile)
    fun showBadges(state: State, badges: List<Badge> = emptyList())
    fun showSuccessLeaveClusterAlert(name: String?)
    fun showRequestClusterLayout()
    fun showFailedLeaveClusterAlert(name: String?)
    fun showFailedGetProfileAlert()
    fun showVerifikasiScreen(step: VerificationStep)
    fun showFailedGetVerifikasi()
}