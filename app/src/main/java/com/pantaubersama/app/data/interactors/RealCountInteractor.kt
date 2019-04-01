package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.data.model.tps.realcount.Candidate
import com.pantaubersama.app.data.model.tps.realcount.Party
import com.pantaubersama.app.data.model.tps.realcount.RealCount
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
    fun savePresidentRealCount(candidate1Count: Long, candidate2Count: Long, invalidCount: Long, tpsId: String, realCountType: String): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            realCount?.candidates?.get(0)?.totalVote = candidate1Count
            realCount?.candidates?.get(1)?.totalVote = candidate2Count
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
            candidates.add(Candidate(0, candidate1Count))
            candidates.add(Candidate(1, candidate2Count))
            val parties: MutableList<Party> = ArrayList()

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(newId.toString(), tpsId, "presiden", candidates, invalidCount, parties)
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

    fun saveDprRealCount(tpsId: String, realCountType: String, items: MutableList<CandidateData>, invalidVote: Long?): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            if (invalidVote != null) {
                realCount?.invalidVote = invalidVote
            }

            items.forEachIndexed { i, partyData ->
                realCount?.parties?.let {
                    it.forEachIndexed { j, partyDb ->
                        if (partyDb.id == partyData.id) {
                            realCount.parties[j] = Party(partyData.id, partyData.totalCount)
                        }
                    }
                }
                realCount?.candidates?.let {
                    it.forEachIndexed { k, candidateDb ->
                        partyData.candidates.forEachIndexed { l, candidateData ->
                            if (candidateDb.id == candidateData.id) {
                                realCount.candidates[k] = Candidate(partyData.candidates[l].id, partyData.candidates[l].candidateCount)
                            }
                        }
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

            items.forEach {
                parties.add(Party(it.id, it.totalCount))
                it.candidates.forEachIndexed { index, candidate ->
                    candidates.add(Candidate(candidate.id, candidate.candidateCount))
                }
            }

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(
                        newId.toString(),
                        tpsId,
                        realCountType,
                        candidates,
                        if (invalidVote != null) {
                            invalidVote
                        } else {
                            0
                        },
                        parties)
                )
            }
        }
    }

    fun saveDpdRealCount(
        tpsId: String,
        realCountType: String,
        candidates: MutableList<com.pantaubersama.app.data.model.tps.candidate.Candidate>,
        invalidVote: Long?
    ): Completable {
        if (appDB.getRealCountDao().getRealCount(tpsId, realCountType) != null) {
            val realCount = appDB.getRealCountDao().getRealCount(tpsId, realCountType)
            if (invalidVote != null) {
                realCount?.invalidVote = invalidVote
            }

            candidates.forEachIndexed { i, candidateData ->
                realCount?.candidates?.let {
                    it.forEachIndexed { j, candidateDb ->
                        if (candidateDb.id == candidateData.id) {
                            realCount.candidates[j] = Candidate(candidateData.id, candidateData.candidateCount)
                        }
                    }
                }
            }
            return Completable.fromCallable {
                realCount?.let {
                    appDB.getRealCountDao().updateRealCount(it)
                }
            }
        } else {
            val newCandidates: MutableList<Candidate> = ArrayList()
            val parties: MutableList<Party> = ArrayList()

            candidates.forEach {
                newCandidates.add(Candidate(it.id, it.candidateCount))
            }

            var newId: Int = 0

            appDB.getRealCountDao().getRealCounts().forEachIndexed { index, realCount ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getRealCountDao().saveRealCount(
                    RealCount(
                        newId.toString(),
                        tpsId,
                        realCountType,
                        newCandidates,
                        if (invalidVote != null) {
                            invalidVote
                        } else {
                            0
                        },
                        parties)
                )
            }
        }
    }

    fun getApiRealCount(tpsId: String, realCountType: String): Single<RealCount> {
        return apiWrapper.getPantauApi().getRealCount(tpsId, realCountType)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.calculation
            }
    }
}