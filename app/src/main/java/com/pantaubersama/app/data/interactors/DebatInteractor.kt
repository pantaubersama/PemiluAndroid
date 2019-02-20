package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.image.Medium
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import java.util.Random
import javax.inject.Inject

/**
 * @author edityomurti on 15/02/2019 12:32
 */
class DebatInteractor @Inject constructor(
    private val rxSchedulers: RxSchedulers
) {
    fun getMessage(): Single<MutableList<Message>> {
        val messageList: MutableList<Message> = ArrayList()
        for (i in 1..15) {
            val message = Message("msg-id-$i", "$i. Mau nambahin, jumlah baliho juga melanggar ketentuan di pertigaan Ahmad Yani sudah saya lampiran bukti foto serta tambahan lokasi baliho dimana saja pemasangan baliho.", Profile(),
                false, i, System.currentTimeMillis())
            messageList.add(message)
        }
        return Single.fromCallable { messageList }.subscribeOn(rxSchedulers.io()).observeOn(rxSchedulers.mainThread())
    }

    fun getKomentar(): Single<MutableList<Komentar>> {
        val komentarList: MutableList<Komentar> = ArrayList()
        for (i in 1..15) {
            val komentar = Komentar("komentar-id-$i", "terlalu membawa subjektifitas yang tidak bisa dipakai buat diskusi yang membangun. saya tandai orang itu.", "${16 - i} menit yang lalu", Profile(fullName = "Netizen $i", avatar = Image(medium = Medium("https://via.placeholder.com/120/${Random().nextInt(256)}/fff?text=N$i"))))
            komentarList.add(komentar)
        }
        return Single.fromCallable { komentarList }.subscribeOn(rxSchedulers.io()).observeOn(rxSchedulers.mainThread())
    }
}