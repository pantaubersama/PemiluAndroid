package com.pantaubersama.app.ui.home

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Profile

/**
 * @author edityomurti on 17/12/2018 02:20
 */
interface HomeView : BaseView {
    fun onSuccessLoadUser(profile: Profile)
}