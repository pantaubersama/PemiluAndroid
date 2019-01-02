package com.pantaubersama.app.ui.profile.setting.badge

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.user.Badge

interface BadgeView : BaseView {
    fun showBadges(badges: List<Badge>)
}