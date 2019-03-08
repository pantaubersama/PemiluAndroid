package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class RealCountInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    fun savePresidentCandidate1Count(candidate1Count: Int, tpsId: String): Single<RealCount> {
        if (appDB.getRealCountDao().getRealCount(tpsId) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId)
            realCount?.candidates?.get(0)?.totalVote = candidate1Count
            realCount?.let {
                appDB.getRealCountDao().saveRealCount(
                    it
                )
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, candidate1Count))
            candidates.add(Candidate(2, 0))
            val parties: MutableList<Party> = ArrayList()
            if (appDB.getRealCountDao().getRealCounts().size != 0) {
                val id = (appDB.getRealCountDao().getRealCounts()[appDB.getRealCountDao().getRealCounts().size - 1].id.toInt() + 1).toString()
                appDB.getRealCountDao().saveRealCount(
                    RealCount(id, tpsId, "presiden", candidates, 0, parties)
                )
            } else {
                appDB.getRealCountDao().saveRealCount(
                    RealCount("1", tpsId, "presiden", candidates, 0, parties)
                )
            }
        }
        return Single.fromCallable {
            appDB.getRealCountDao().getRealCount(tpsId)
        }
    }

    fun savePresidentCandidate2Count(candidate2Count: Int, tpsId: String): Single<RealCount> {
        if (appDB.getRealCountDao().getRealCount(tpsId) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId)
            realCount?.candidates?.get(1)?.totalVote = candidate2Count
            realCount?.let {
                appDB.getRealCountDao().saveRealCount(
                    it
                )
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, 0))
            candidates.add(Candidate(2, candidate2Count))
            val parties: MutableList<Party> = ArrayList()
            if (appDB.getRealCountDao().getRealCounts().size != 0) {
                val id = (appDB.getRealCountDao().getRealCounts()[appDB.getRealCountDao().getRealCounts().size - 1].id.toInt() + 1).toString()
                appDB.getRealCountDao().saveRealCount(
                    RealCount(id, tpsId, "presiden", candidates, 0, parties)
                )
            } else {
                appDB.getRealCountDao().saveRealCount(
                    RealCount("1", tpsId, "presiden", candidates, 0, parties)
                )
            }
        }
        return Single.fromCallable {
            appDB.getRealCountDao().getRealCount(tpsId)
        }
    }

    fun saveInvalidCount(invalidCount: Int, tpsId: String): Single<RealCount> {
        if (appDB.getRealCountDao().getRealCount(tpsId) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId)
            realCount?.invalidVote = invalidCount
            realCount?.let {
                appDB.getRealCountDao().saveRealCount(
                    it
                )
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, 0))
            candidates.add(Candidate(2, 0))
            val parties: MutableList<Party> = ArrayList()
            if (appDB.getRealCountDao().getRealCounts().size != 0) {
                val id = (appDB.getRealCountDao().getRealCounts()[appDB.getRealCountDao().getRealCounts().size - 1].id.toInt() + 1).toString()
                appDB.getRealCountDao().saveRealCount(
                    RealCount(id, tpsId, "presiden", candidates, invalidCount, parties)
                )
            } else {
                appDB.getRealCountDao().saveRealCount(
                    RealCount("1", tpsId, "presiden", candidates, invalidCount, parties)
                )
            }
        }
        return Single.fromCallable {
            appDB.getRealCountDao().getRealCount(tpsId)
        }
    }

    fun getRealCount(tpsId: String): RealCount? {
        return appDB.getRealCountDao().getRealCount(tpsId)
    }
}