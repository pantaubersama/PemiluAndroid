package com.pantaubersama.app.ui.profile.setting.badge.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.AchievedBadge

interface DetailBadgeView : BaseView {
    fun showBadge(item: AchievedBadge)
    fun onFailedGetBadge(throwable: Throwable)
}