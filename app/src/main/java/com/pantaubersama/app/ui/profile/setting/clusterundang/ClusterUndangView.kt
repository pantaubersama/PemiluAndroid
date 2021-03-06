package com.pantaubersama.app.ui.profile.setting.clusterundang

import com.pantaubersama.app.base.BaseView

interface ClusterUndangView : BaseView {
    fun showSuccessInviteAlert()
    fun showFailedInviteAlert()
    fun finishThisSection()
}