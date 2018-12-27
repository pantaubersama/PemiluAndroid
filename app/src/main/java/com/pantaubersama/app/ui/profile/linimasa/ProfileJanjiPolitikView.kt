package com.pantaubersama.app.ui.profile.linimasa

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik

/**
 * @author edityomurti on 27/12/2018 15:29
 */
interface ProfileJanjiPolitikView : BaseView {
    fun bindDataJanjiPolitik(janpolList: MutableList<JanjiPolitik>?)
}