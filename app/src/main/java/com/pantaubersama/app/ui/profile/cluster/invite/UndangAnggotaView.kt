package com.pantaubersama.app.ui.profile.cluster.invite

import com.pantaubersama.app.base.BaseView

interface UndangAnggotaView : BaseView {
    fun showSuccessInviteAlert()
    fun showFailedInviteAlert()
    fun disableView()
    fun enableView()
    fun removeEmail()
    fun reverseView(enable: Boolean)
    fun showSuccessDisableUrlInviteAlert()
    fun showSuccessEnableUrlInviteAlert()
    fun showFailedDisableUrlInviteAlert()
    fun showFailedEnableUrlInviteAlert()
}