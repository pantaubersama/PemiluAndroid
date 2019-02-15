package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 15/02/2019 12:32
 */
class DebatInteractor @Inject constructor(
    private val rxSchedulers: RxSchedulers
) {
    fun getMessage(): Single<MutableList<Message>> {
        var messageList: MutableList<Message> = ArrayList()
        for (i in 1..15) {
            val message = Message("msg-id-$i", "apa $i?", User())
            messageList.add(message)
        }
        return Single.fromCallable { messageList }.subscribeOn(rxSchedulers.io()).observeOn(rxSchedulers.mainThread())
    }
}