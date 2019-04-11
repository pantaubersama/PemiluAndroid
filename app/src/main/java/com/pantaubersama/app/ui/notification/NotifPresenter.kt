package com.pantaubersama.app.ui.notification

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.NotificationInteractor
import javax.inject.Inject

class NotifPresenter @Inject constructor(private val notificationInteractor: NotificationInteractor) : BasePresenter<NotifView>() {
    fun getNotifications(page: Int, perPage: Int) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            notificationInteractor.getNotifications(page, perPage)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it.isNotEmpty()) {
                                view?.bindNotifications(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.setDataEnd()
                                view?.showEmptyDataAlert()
                            }
                        } else {
                            if (it.isNotEmpty()) {
                                view?.bindNextNotifications(it)
                                if (it.size < perPage) {
                                    view?.setDataEnd()
                                }
                            } else {
                                view?.setDataEnd()
                            }
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