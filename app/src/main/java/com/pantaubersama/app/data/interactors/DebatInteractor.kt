package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.data.model.user.Profile
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
            val message = Message("msg-id-$i", "$i. Mau nambahin, jumlah baliho juga melanggar ketentuan di pertigaan Ahmad Yani sudah saya lampiran bukti foto serta tambahan lokasi baliho dimana saja pemasangan baliho.", Profile(),
                false, i, System.currentTimeMillis())
            messageList.add(message)
        }
        return Single.fromCallable { messageList }.subscribeOn(rxSchedulers.io()).observeOn(rxSchedulers.mainThread())
    }
}