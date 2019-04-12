package com.pantaubersama.app.ui.notification

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.notification.NotificationWhole

interface NotifView : BaseView {
    fun showFailedLoadNotificationAlert()
    fun bindNotifications(notifications: MutableList<NotificationWhole>)
    fun showEmptyDataAlert()
    fun setDataEnd()
    fun bindNextNotifications(notifications: MutableList<NotificationWhole>)
}