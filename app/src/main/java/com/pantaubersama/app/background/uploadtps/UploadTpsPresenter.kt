package com.pantaubersama.app.background.uploadtps

import com.pantaubersama.app.data.interactors.TPSInteractor
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class UploadTpsPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) {
    private var view: UploadTpsView? = null
    var disposables: CompositeDisposable = CompositeDisposable()

    fun init(view: UploadTpsView) {
        this.view = view
    }

    fun uploadTpsData(tpsId: String) {
        disposables.add(
            tpsInteractor.uploadTps(tpsId)
                .subscribe(
                    {
                        view?.increaseProgress(5)
                        uploadRealCount(it.id, tpsId, "presiden")
                    },
                    {
                        view?.showError(it)
                        view?.showFailed()
                    }
                )
        )
    }

    private fun uploadRealCount(apiTpsId: String, dbTpsId: String, realCountType: String) {
        disposables.add(
            tpsInteractor.uploadRealCount(apiTpsId, dbTpsId, realCountType)
                .subscribe(
                    {
                        view?.increaseProgress(5)
                        when (realCountType) {
                            "presiden" -> uploadRealCount(apiTpsId, dbTpsId, "dpr")
                            "dpr" -> uploadRealCount(apiTpsId, dbTpsId, "dpd")
                            "dpd" -> uploadRealCount(apiTpsId, dbTpsId, "provinsi")
                            "provinsi" -> uploadRealCount(apiTpsId, dbTpsId, "kabupaten")
                            "kabupaten" -> {
                                Timber.d("all_realcount_uploaded")
                            }
                        }
                    },
                    {
                        view?.showError(it)
                        view?.showFailed()
                    }
                )
        )
    }
}