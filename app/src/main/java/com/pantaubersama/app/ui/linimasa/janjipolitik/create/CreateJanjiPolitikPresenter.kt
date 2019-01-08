package com.pantaubersama.app.ui.linimasa.janjipolitik.create

import android.os.Handler
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.interactors.ProfileInteractor
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 23:21
 */
class CreateJanjiPolitikPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val janjiPolitikInteractor: JanjiPolitikInteractor
) : BasePresenter<CreateJanjiPolitikView>() {
    fun getProfile() {
        view?.onSuccessGetProfile(profileInteractor.getProfile())
    }

    fun createJanpol(title: RequestBody, body: RequestBody, image: MultipartBody.Part?) {
        view?.showLoading()
        disposables?.add(janjiPolitikInteractor.createJanjiPolitik(title, body, image)
            .subscribe(
                {
                    Handler().postDelayed({ /* @edityo 8/1/19  delayed bcs new created Janpol not showing directly on GET respoonse in JanPolFragment */
                        view?.onSuccessCreateJanpol(it)
                    }, 1000)
                },
                {
                    view?.dismissLoading()
                    view?.onFailedCreateJanpol()
                    view?.showError(it)
                }
            ))
    }
}