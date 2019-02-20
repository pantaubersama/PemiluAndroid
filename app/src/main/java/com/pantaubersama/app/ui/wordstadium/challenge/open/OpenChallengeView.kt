package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface OpenChallengeView : BaseView {
    fun showProfile(profile: Profile)
    fun onSuccessOpenChallenge()
}