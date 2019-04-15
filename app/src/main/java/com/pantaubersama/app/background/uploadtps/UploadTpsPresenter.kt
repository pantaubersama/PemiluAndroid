package com.pantaubersama.app.background.uploadtps

import com.pantaubersama.app.data.interactors.TPSInteractor
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UploadTpsPresenter @Inject constructor(
    private val tpsInteractor: TPSInteractor
) {
    private var view: UploadTpsView? = null
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var currentRealCountUpload = 0
    private var currentC1Upload = 0
    private var currentImageDocsPresiden = 0
    private var currentImageDocsDpr = 0
    private var currentImageDocsDpd = 0
    private var currentImageDocsDprdProv = 0
    private var currentImageDocsDprdKab = 0
    private var currentImageDocsSuasanaTps = 0
    private var successUpload = 0
    private var errorMessage = ""

    fun init(view: UploadTpsView) {
        this.view = view
    }

    fun uploadTpsData(tpsId: String) {
        disposables.add(
            tpsInteractor.uploadTps(tpsId)
                .subscribe(
                    {
                        view?.setMaxProgress(
                            tpsInteractor.getRealCountsSize(tpsId) +
                                tpsInteractor.getC1sSize(tpsId) +
                                tpsInteractor.getImageDocsSize(tpsId) + 1
                        )
                        uploadRealCount(it.id, tpsId)
                    },
                    {
                        view?.showError(it)
                        view?.showFailed(it.message)
                    }
                )
        )
    }

    private fun uploadRealCount(apiTpsId: String, dbTpsId: String) {
        if (currentRealCountUpload<tpsInteractor.getRealCountsSize(dbTpsId)) {
            val realcount = tpsInteractor.getRealCounts(dbTpsId).get(currentRealCountUpload)
            realcount.hitungRealCountId = apiTpsId
            disposables.add(
                tpsInteractor.uploadRealCount(realcount)
                    .doOnEvent {
                        view?.increaseProgress(1,
                            when (realcount.calculationType) {
                                "presiden" -> "Mengunggah Perhitungan - Presiden"
                                "dpr" -> "Mengunggah Perhitungan - DPR RI"
                                "dpd" -> "Mengunggah Perhitungan - DPD"
                                "provinsi" -> "Mengunggah Perhitungan - DPRD Tingkat Provinsi"
                                else -> "Mengunggah Perhitungan - DPRD Tingkat Kabupaten"
                            }
                            )
                        currentRealCountUpload++
                        uploadRealCount(apiTpsId, dbTpsId)
                    }
                    .subscribe(
                        {
                            successUpload++
                        },
                        {
                            errorMessage = it.message.toString()
                            view?.showError(it)
                        }
                    )
            )
        } else {
            uploadC1(apiTpsId, dbTpsId)
        }
    }

    private fun uploadC1(apiTpsId: String, dbTpsId: String) {
        if (currentC1Upload < tpsInteractor.getC1sSize(dbTpsId)) {
            val c1 = tpsInteractor.getC1s(dbTpsId).get(currentC1Upload)
            c1.tpsId = apiTpsId
            disposables.add(
                tpsInteractor.uploadC1(c1)
                    .doOnEvent {
                        view?.increaseProgress(
                            1,
                            when (c1.formC1Type) {
                                "presiden" -> "Mengunggah Form C1 - Presiden"
                                "dpr" -> "Mengunggah Form C1 - DPR RI"
                                "dpd" -> "Mengunggah Form C1 - DPD"
                                "provinsi" -> "Mengunggah Form C1 - DPRD Tingkat Provinsi"
                                else -> "Mengunggah Form C1 - DPRD Tingkat Kabupaten"
                            }
                        )
                        currentC1Upload++
                        uploadC1(apiTpsId, dbTpsId)
                    }
                    .subscribe(
                        {
                            successUpload++
                        },
                        {
                            view?.showError(it)
                            errorMessage = it.message.toString()
                        }
                    )
            )
        } else {
            uploadImages(apiTpsId, dbTpsId)
        }
    }

    private fun uploadImages(apiTpsId: String, dbTpsId: String) {
        val image = tpsInteractor.getImageDocs(dbTpsId)
        image?.let {
            if (currentImageDocsPresiden < image.presiden.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "c1_presiden",
                        image.presiden[currentImageDocsPresiden].uri
                    )
                        .doOnEvent {
                            currentImageDocsPresiden++
                            view?.increaseProgress(1, "Mengunggah Gambar C1 - Presiden ($currentImageDocsPresiden)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else if (currentImageDocsDpr < image.dpr.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "c1_dpr_ri",
                        image.dpr[currentImageDocsDpr].uri
                    )
                        .doOnEvent {
                            currentImageDocsDpr++
                            view?.increaseProgress(1, "Mengunggah Gambar C1 - DPR RI ($currentImageDocsDpr)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else if (currentImageDocsDpd < image.dpd.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "c1_dpd",
                        image.dpd[currentImageDocsDpd].uri
                    )
                        .doOnEvent {
                            currentImageDocsDpd++
                            view?.increaseProgress(1, "Mengunggah Gambar C1 - DPD ($currentImageDocsDpd)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else if (currentImageDocsDprdProv < image.dprdProv.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "c1_dprd_provinsi",
                        image.dprdProv[currentImageDocsDprdProv].uri
                    )
                        .doOnEvent {
                            currentImageDocsDprdProv++
                            view?.increaseProgress(1, "Mengunggah Gambar C1 - DPRD Tingkat Provinsi ($currentImageDocsDprdProv)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else if (currentImageDocsDprdKab < image.dprdKab.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "c1_dprd_kabupaten",
                        image.dprdKab[currentImageDocsDprdKab].uri
                    )
                        .doOnEvent {
                            currentImageDocsDprdKab++
                            view?.increaseProgress(1, "Mengunggah Gambar C1 - DPRD Tingkat Kabupaten ($currentImageDocsDprdKab)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else if (currentImageDocsSuasanaTps < image.suasanaTps.size) {
                disposables.add(
                    tpsInteractor.uploadImage(
                        apiTpsId,
                        "suasana_tps",
                        image.suasanaTps[currentImageDocsSuasanaTps].uri
                    )
                        .doOnEvent {
                            currentImageDocsSuasanaTps++
                            view?.increaseProgress(1, "Mengunggah Gambar Suasana TPS ($currentImageDocsSuasanaTps)")
                            uploadImages(apiTpsId, dbTpsId)
                        }
                        .subscribe(
                            {
                                successUpload++
                            },
                            {
                                view?.showError(it)
                                errorMessage = it.message.toString()
                            }
                        )
                )
            } else {

                if (successUpload == tpsInteractor.getRealCountsSize(dbTpsId) +
                    tpsInteractor.getC1sSize(dbTpsId) +
                    tpsInteractor.getImageDocsSize(dbTpsId) - 1) {
                    view?.increaseProgress(1, "Menyelesaikan Unggahan")
                    disposables.add(
                        tpsInteractor.publishRealCount(apiTpsId, dbTpsId)
                            .subscribe(
                                {
                                    view?.onSuccessPublishTps()
                                },
                                {
                                    view?.showError(it)
                                    errorMessage = it.message.toString()
                                }
                            )
                    )
                } else {
                    view?.showFailed(errorMessage)
                }
            }
        }
    }

    fun detach() {
        disposables.clear()
        view = null
    }
}