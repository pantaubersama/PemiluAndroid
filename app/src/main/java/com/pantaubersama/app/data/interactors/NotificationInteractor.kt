package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class NotificationInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun getNotifications(): Single<MutableList<NotificationWhole>> {
        return apiWrapper.getNotificationApi().getNotifications()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map { it.data.notifications }
    }
}