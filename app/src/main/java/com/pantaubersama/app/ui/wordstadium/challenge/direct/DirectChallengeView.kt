package com.pantaubersama.app.ui.wordstadium.challenge.direct

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.LawanDebat
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink

interface DirectChallengeView : BaseView{
    fun showProfile(profile: Profile)
    fun onSuccessConvertLink(oEmbedLink: OEmbedLink)
    fun bindData(users: MutableList<LawanDebat>)
    fun showEmptyData()
    fun showFailedGetDataAlert()
}