package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class RealCountInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    fun savePresidentCandidate1Count(candidate1Count: Int, tpsId: String, realCountType: String): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            realCount?.candidates?.get(0)?.totalVote = candidate1Count
            return Completable.fromCallable {
                realCount?.let {
                    appDB.getRealCountDao().updateRealCount(
                        it
                    )
                }
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, candidate1Count))
            candidates.add(Candidate(2, 0))
            val parties: MutableList<Party> = ArrayList()

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(newId.toString(), tpsId, "presiden", candidates, 0, parties)
                )
            }
        }
    }

    fun savePresidentCandidate2Count(candidate2Count: Int, tpsId: String, realCountType: String): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            realCount?.candidates?.get(1)?.totalVote = candidate2Count
            return Completable.fromCallable {
                realCount?.let {
                    appDB.getRealCountDao().updateRealCount(
                        it
                    )
                }
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, 0))
            candidates.add(Candidate(2, candidate2Count))
            val parties: MutableList<Party> = ArrayList()

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }

            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(newId.toString(), tpsId, "presiden", candidates, 0, parties)
                )
            }
        }
    }

    fun saveInvalidCount(invalidCount: Int, tpsId: String, realCountType: String): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            realCount?.invalidVote = invalidCount
            return Completable.fromCallable {
                realCount?.let {
                    appDB.getRealCountDao().updateRealCount(
                        it
                    )
                }
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            candidates.add(Candidate(1, 0))
            candidates.add(Candidate(2, 0))
            val parties: MutableList<Party> = ArrayList()

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }

            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(newId.toString(), tpsId, "presiden", candidates, 0, parties)
                )
            }
        }
    }

    fun getPresidentRealCount(tpsId: String, realCountType: String): RealCount? {
        return appDB.getRealCountDao().getRealCount(tpsId, realCountType)
    }

    fun getDapil(tps: TPS, realCountType: String): Single<Dapil> {
        return apiWrapper.getPantauApi().getDapil(
            tps.province.code,
            tps.regency.code,
            tps.district.code,
            realCountType
        )
            .map {
                it.data
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getRealCountList(id: Int, realCountType: String): Single<MutableList<CandidateData>> {
        return apiWrapper.getPantauApi().getRealCountList(id, realCountType)
            .map {
                it.data
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getRealCount(tpsId: String, realCountType: String): RealCount? {
        return appDB.getRealCountDao().getRealCount(tpsId, realCountType)
    }

    fun saveRealCount(tpsId: String, realCountType: String, item: CandidateData): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            realCount?.candidates?.let {
                it.forEachIndexed { i, candidateDb ->
                    item.candidates.forEachIndexed { j, candidateData ->
                        if (candidateDb.id == candidateData.id) {
                            realCount.candidates[i].totalVote = candidateData.candidateCount
                        }
                    }
                }
            }
            realCount?.parties?.let {
                it.forEachIndexed { i, party ->
                    if (party.id == item.id) {
                        realCount.parties[i].totalVote = item.totalCount
                    }
                }
            }
            return Completable.fromCallable {
                realCount?.let {
                    appDB.getRealCountDao().updateRealCount(it)
                }
            }
        } else {
            val candidates: MutableList<Candidate> = ArrayList()
            val parties: MutableList<Party> = ArrayList()

            parties.add(Party(item.id, item.totalCount))

            item.candidates.forEachIndexed { index, candidate ->
                candidates.add(Candidate(candidate.id, candidate.candidateCount))
            }

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(newId.toString(), tpsId, realCountType, candidates, 0, parties)
                )
            }
        }
    }
}