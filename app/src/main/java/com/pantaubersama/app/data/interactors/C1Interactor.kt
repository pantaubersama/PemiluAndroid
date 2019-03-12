package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class C1Interactor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    fun saveSection1(
        tpsId: String,
        a3L: Int,
        a3P: Int,
        a4L: Int,
        a4P: Int,
        aDpkL: Int,
        aDpkP: Int,
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

            appDB.getC1Dao().getC1s(tpsId, c1Type).forEachIndexed { index, realCount ->
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
}