package com.pantaubersama.app.ui.note.partai

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.model.user.Profile

interface PartaiView : BaseView {
    fun bindUserProfile(profile: Profile)
    fun showPartai(politicalParties: List<PoliticalParty>)
}