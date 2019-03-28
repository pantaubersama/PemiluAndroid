package com.pantaubersama.app.background.uploadtps

import com.pantaubersama.app.data.interactors.TPSInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
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
                        view?.showFailed(it.message)
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
                            uploadC1(apiTpsId, dbTpsId, "presiden")
                        }
                    }
                },
                {
                    view?.showError(it)
                    view?.showFailed(it.message)
                }
            ).addTo(disposables)
        } else {
            view?.increaseProgress(5)
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
                            uploadImages(apiTpsId, dbTpsId, "presiden")
                        }
                    }
                },
                {
                    view?.showError(it)
                    view?.showFailed(it.message)
                }
            ).addTo(disposables)
        } else {
            view?.increaseProgress(5)
            when (c1Type) {
                "presiden" -> uploadC1(apiTpsId, dbTpsId, "dpr")
                "dpr" -> uploadC1(apiTpsId, dbTpsId, "dpd")
                "dpd" -> uploadC1(apiTpsId, dbTpsId, "provinsi")
                "provinsi" -> uploadC1(apiTpsId, dbTpsId, "kabupaten")
                "kabupaten" -> {
                    uploadImages(apiTpsId, dbTpsId, "c1_presiden")
                }
            }
        }
    }

    private fun uploadImages(apiTpsId: String, dbTpsId: String, imagesUploadType: String) {
        val images = tpsInteractor.getImagesWithType(dbTpsId, imagesUploadType)
        if (images != null) {
            if (images.size != 0) {
                images.forEachIndexed { i, it ->
                    disposables.add(
                        tpsInteractor.uploadImage(apiTpsId, imagesUploadType, it.uri)
                            .subscribe(
                                {
                                    if (i == images.size - 1) {
                                        view?.increaseProgress(5)
                                        when (imagesUploadType) {
                                            "c1_presiden" -> uploadImages(apiTpsId, dbTpsId, "c1_dpr_ri")
                                            "c1_dpr_ri" -> uploadImages(apiTpsId, dbTpsId, "c1_dpd")
                                            "c1_dpd" -> uploadImages(apiTpsId, dbTpsId, "c1_dprd_provinsi")
                                            "c1_dprd_provinsi" -> uploadImages(apiTpsId, dbTpsId, "c1_dprd_kabupaten")
                                            "c1_dprd_kabupaten" -> uploadImages(apiTpsId, dbTpsId, "suasana_tps")
                                            "suasana_tps" -> {
                                                publishRealCount(apiTpsId, dbTpsId)
                                            }
                                        }
                                    }
                                },
                                {
                                    view?.showError(it)
                                    view?.showFailed(it.message)
                                }
                            )
                    )
                }
            } else {
                view?.increaseProgress(5)
                when (imagesUploadType) {
                    "c1_presiden" -> uploadImages(apiTpsId, dbTpsId, "c1_dpr_ri")
                    "c1_dpr_ri" -> uploadImages(apiTpsId, dbTpsId, "c1_dpd")
                    "c1_dpd" -> uploadImages(apiTpsId, dbTpsId, "c1_dprd_provinsi")
                    "c1_dprd_provinsi" -> uploadImages(apiTpsId, dbTpsId, "c1_dprd_kabupaten")
                    "c1_dprd_kabupaten" -> uploadImages(apiTpsId, dbTpsId, "suasana_tps")
                    "suasana_tps" -> {
                        publishRealCount(apiTpsId, dbTpsId)
                    }
                }
            }
        } else {
            view?.increaseProgress(5)
            publishRealCount(apiTpsId, dbTpsId)
        }
    }

    private fun publishRealCount(apiTpsId: String, dbTpsId: String) {
        disposables.add(
            tpsInteractor.publishRealCount(apiTpsId, dbTpsId)
                .subscribe(
                    {
                        view?.onSuccessPublishTps()
                    },
                    {
                        view?.showError(it)
                        view?.showFailed(it.message)
                    }
                )
        )
    }

    fun dispatch() {
        disposables.clear()
        view = null
    }
}