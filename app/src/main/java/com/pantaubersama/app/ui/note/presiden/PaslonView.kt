package com.pantaubersama.app.ui.note.presiden

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

interface PaslonView : BaseView {
    fun bindUserProfile(profile: Profile)

}