package com.pantaubersama.app.ui.notification

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.NotificationInteractor
import javax.inject.Inject

class NotifPresenter @Inject constructor(private val notificationInteractor: NotificationInteractor) : BasePresenter<NotifView>() {
    fun getNotifications() {
        view?.showLoading()
        disposables.add(
            notificationInteractor.getNotifications()
                .subscribe(
                    {
                        view?.dismissLoading()
                        if (it.isNotEmpty()) {
                            view?.bindNotifications(it)
                        } else {
                            view?.showEmptyDataAlert()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showError(it)
                        view?.showFailedLoadNotificationAlert()
                    }
                )
        )
    }
}