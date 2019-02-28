package com.pantaubersama.app.ui.note

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.model.user.Profile

interface CatatanPilihanView : BaseView {
    fun bindUserProfile(profile: Profile)
    fun bindMyTendency(tendency: KuisUserResult, name: String)
    fun showFailedGetMyTendencyAlert()
    fun showSuccessSubmitCatatanAlert()
    fun finishActivity()
    fun showFailedSubmitCatatanAlert()
    fun showPartai(parties: MutableList<PoliticalParty>)
    fun showPartiesProgressBar()
    fun dismissPartiesProgressBar()
    fun showFailedGetPartiesAlert()
}