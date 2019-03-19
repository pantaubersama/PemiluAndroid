package com.pantaubersama.app.background.uploadtps

import com.pantaubersama.app.data.interactors.TPSInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
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
        val completable = tpsInteractor.uploadRealCount(apiTpsId, dbTpsId, realCountType)
        if (completable != null) {
            completable.subscribe(
                {
                    view?.increaseProgress(5)
                    when (realCountType) {
                        "presiden" -> uploadRealCount(apiTpsId, dbTpsId, "dpr")
                        "dpr" -> uploadRealCount(apiTpsId, dbTpsId, "dpd")
                        "dpd" -> uploadRealCount(apiTpsId, dbTpsId, "provinsi")
                        "provinsi" -> uploadRealCount(apiTpsId, dbTpsId, "kabupaten")
                        "kabupaten" -> {
//                            uploadC1(apiTpsId, dbTpsId, "presiden")
                        }
                    }
                },
                {
                    view?.showError(it)
                    view?.showFailed()
                }
            ).addTo(disposables)
        } else {
            when (realCountType) {
                "presiden" -> uploadRealCount(apiTpsId, dbTpsId, "dpr")
                "dpr" -> uploadRealCount(apiTpsId, dbTpsId, "dpd")
                "dpd" -> uploadRealCount(apiTpsId, dbTpsId, "provinsi")
                "provinsi" -> uploadRealCount(apiTpsId, dbTpsId, "kabupaten")
                "kabupaten" -> {
                    uploadC1(apiTpsId, dbTpsId, "presiden")
                }
            }
        }
    }

    private fun uploadC1(apiTpsId: String, dbTpsId: String, c1Type: String) {
        val completable = tpsInteractor.uploadC1(apiTpsId, dbTpsId, c1Type)
        if (completable != null) {
            completable.subscribe(
                {
                    view?.increaseProgress(5)
                    when (c1Type) {
                        "presiden" -> uploadC1(apiTpsId, dbTpsId, "dpr")
                        "dpr" -> uploadC1(apiTpsId, dbTpsId, "dpd")
                        "dpd" -> uploadC1(apiTpsId, dbTpsId, "provinsi")
                        "provinsi" -> uploadC1(apiTpsId, dbTpsId, "kabupaten")
                        "kabupaten" -> {
                            Timber.d("all_c1_uploaded")
                        }
                    }
                },
                {
                    view?.showError(it)
                    view?.showFailed()
                }
            ).addTo(disposables)
        } else {
            when (c1Type) {
                "presiden" -> uploadC1(apiTpsId, dbTpsId, "dpr")
                "dpr" -> uploadC1(apiTpsId, dbTpsId, "dpd")
                "dpd" -> uploadC1(apiTpsId, dbTpsId, "provinsi")
                "provinsi" -> uploadC1(apiTpsId, dbTpsId, "kabupaten")
                "kabupaten" -> {
                    Timber.d("all_c1_uploaded")
                }
            }
        }
    }
}