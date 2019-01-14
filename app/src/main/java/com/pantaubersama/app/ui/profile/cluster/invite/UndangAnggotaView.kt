package com.pantaubersama.app.ui.profile.cluster.invite

import com.pantaubersama.app.base.BaseView

interface UndangAnggotaView : BaseView {
    fun showSuccessInviteAlert()
    fun showFailedInviteAlert()
    fun disableView()
    fun enableView()
    fun removeEmail()
}