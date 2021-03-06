package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.c1.C1Form
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class C1Interactor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    fun saveSection1(
        tpsId: String,
        a3L: Long,
        a3P: Long,
        a4L: Long,
        a4P: Long,
        aDpkL: Long,
        aDpkP: Long,
        c1Type: String
    ): Completable {
        if (appDB.getC1Dao().getC1(tpsId, c1Type) != null) {
            val c1 = appDB.getC1Dao().getC1(tpsId, c1Type)
            c1?.a3L = a3L
            c1?.a3P = a3P
            c1?.a4L = a4L
            c1?.a4P = a4P
            c1?.aDpkL = aDpkL
            c1?.aDpkP = aDpkP
            return Completable.fromCallable {
                appDB.getC1Dao().updateC1(c1)
            }
        } else {
            var newId: Int = 0

            appDB.getC1Dao().getC1s().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getC1Dao().saveC1(
                    C1Form(
                        newId.toString(),
                        tpsId,
                        c1Type,
                        a3L,
                        a3P,
                        a4L,
                        a4P,
                        aDpkL,
                        aDpkP,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                )
            }
        }
    }

    fun getC1(tpsId: String, c1Type: String): C1Form? {
        return appDB.getC1Dao().getC1(tpsId, c1Type)
    }

    fun saveSection2(
        tpsId: String,
        dptC7L: Long,
        dptC7P: Long,
        dptBC7L: Long,
        dptBC7P: Long,
        dpkC7L: Long,
        dpkC7P: Long,
        c1Type: String
    ): Completable {
        if (appDB.getC1Dao().getC1(tpsId, c1Type) != null) {
            val c1 = appDB.getC1Dao().getC1(tpsId, c1Type)
            c1?.c7DptL = dptC7L
            c1?.c7DptP = dptC7P
            c1?.c7DptBL = dptBC7L
            c1?.c7DptBP = dptBC7P
            c1?.c7DpkL = dpkC7L
            c1?.c7DpkP = dpkC7P
            return Completable.fromCallable {
                appDB.getC1Dao().updateC1(c1)
            }
        } else {
            var newId: Int = 0

            appDB.getC1Dao().getC1s().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getC1Dao().saveC1(
                    C1Form(
                        newId.toString(),
                        tpsId,
                        c1Type,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                    dptC7L,
                    dptC7P,
                    dptBC7L,
                    dptBC7P,
                    dpkC7L,
                    dpkC7P,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                )
            }
        }
    }

    fun saveDisabilitas(tpsId: String, disabilitasL: Long, disabilitasP: Long, c1Type: String): Completable {
        if (appDB.getC1Dao().getC1(tpsId, c1Type) != null) {
            val c1 = appDB.getC1Dao().getC1(tpsId, c1Type)
            c1?.disabilitasTerdaftarL = disabilitasL
            c1?.disabilitasTerdaftarP = disabilitasP
            return Completable.fromCallable {
                appDB.getC1Dao().updateC1(c1)
            }
        } else {
            var newId: Int = 0

            appDB.getC1Dao().getC1s().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getC1Dao().saveC1(
                    C1Form(
                        newId.toString(),
                        tpsId,
                        c1Type,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        disabilitasL,
                        disabilitasP,
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                )
            }
        }
    }

    fun saveHakPilihDisabilitas(tpsId: String, disabilitasL: Long, disabilitasP: Long, c1Type: String): Completable {
        if (appDB.getC1Dao().getC1(tpsId, c1Type) != null) {
            val c1 = appDB.getC1Dao().getC1(tpsId, c1Type)
            c1?.disabilitasHakPilihL = disabilitasL
            c1?.disabilitasHakPilihP = disabilitasP
            return Completable.fromCallable {
                appDB.getC1Dao().updateC1(c1)
            }
        } else {
            var newId: Int = 0

            appDB.getC1Dao().getC1s().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getC1Dao().saveC1(
                    C1Form(
                        newId.toString(),
                        tpsId,
                        c1Type,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        disabilitasL,
                        disabilitasP,
                        0,
                        0,
                        0
                    )
                )
            }
        }
    }

    fun saveSuratSuaraSection(tpsId: String, reject: Long, unused: Long, used: Long, c1Type: String): Completable {
        if (appDB.getC1Dao().getC1(tpsId, c1Type) != null) {
            val c1 = appDB.getC1Dao().getC1(tpsId, c1Type)
            c1?.suratDikembalikan = reject
            c1?.suratTidakDigunakan = unused
            c1?.suratDigunakan = used
            return Completable.fromCallable {
                appDB.getC1Dao().updateC1(c1)
            }
        } else {
            var newId: Int = 0

            appDB.getC1Dao().getC1s().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getC1Dao().saveC1(
                    C1Form(
                        newId.toString(),
                        tpsId,
                        c1Type,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        reject,
                        unused,
                        used
                    )
                )
            }
        }
    }

    fun getC1Remote(tpsId: String, c1Type: String): Single<C1Form> {
        return apiWrapper.getPantauApi().getC1(tpsId, c1Type)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.c1Form
            }
    }
}