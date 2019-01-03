package com.pantaubersama.app.ui.profile.setting.ubahsandi

import com.pantaubersama.app.base.BaseView

interface UbahSandiView : BaseView {
    fun showSuccessUpdatePasswordAlert()
    fun finishActivity()
    fun showFailedUpdatePasswordAlert()
    fun disableView()
    fun enableView()
}