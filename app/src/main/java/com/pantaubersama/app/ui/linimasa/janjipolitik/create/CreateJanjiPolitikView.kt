package com.pantaubersama.app.ui.linimasa.janjipolitik.create

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.user.Profile

/**
 * @author edityomurti on 25/12/2018 23:20
 */
interface CreateJanjiPolitikView : BaseView {
    fun onSuccessGetProfile(profile: Profile)
    fun onSuccessCreateJanpol(janjiPolitik: JanjiPolitik)
    fun onFailedCreateJanpol()
}