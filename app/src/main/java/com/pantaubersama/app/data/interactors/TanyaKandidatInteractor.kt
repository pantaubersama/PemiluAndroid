package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatListData
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatListResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TanyaKandidatInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {
    fun createTanyaKandidat(body: String?): Single<TanyaKandidatListResponse> {
        return apiWrapper
            .getPantauApi()
            .createTanyaKandidat(
                body
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getTanyaKandidatlist(
        page: Int?,
        perPage: Int?
    ): Single<TanyaKandidatListData> {
        return apiWrapper.getPantauApi().getTanyaKandidatList(
            page,
            perPage,
            dataCache.loadTanyaKandidatOrderFilter(),
            dataCache.loadTanyaKandidatOrderFilterDirection(),
            dataCache.loadTanyaKandidatUserFilter()
        )
            .map { it.data }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getMyTanyaKandidatList(
        page: Int,
        perPage: Int
    ): Single<TanyaKandidatListData?> {
        return apiWrapper.getPantauApi().getMyTanyaKandidatList(page, perPage)
            .subscribeOn(rxSchedulers.io())
            .map { it.data }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getTanyaKandidatById(questionId: String): Single<Pertanyaan?> {
        return apiWrapper.getPantauApi().getTanyaKandidatById(questionId)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.question }
            .observeOn(rxSchedulers.mainThread())
    }

    fun upVoteQuestion(id: String?, className: String?): Completable {
        return apiWrapper
            .getPantauApi()
            .upVoteQuestion(
                id,
                className
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun reportQuestion(id: String?, className: String?): Completable {
        return apiWrapper
            .getPantauApi()
            .reportQuestion(
                id,
                className
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun deleteQuestions(id: String?): Completable {
        return apiWrapper
            .getPantauApi()
            .deleteQuestion(
                id
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun saveTanyaKandidatFilter(userFilter: String?, orderFilter: String?): Completable {
        return Completable.fromCallable { dataCache.saveTanyaKandidatUserFilter(userFilter) }
            .andThen(
                Completable.fromCallable { dataCache.saveTanyaKandidatOrderFilter(orderFilter) }
            )
    }

    fun loadTanyaKandidatUserFilter(): Single<String> {
        return Single.fromCallable {
            dataCache.loadTanyaKandidatUserFilter()
        }
    }

    fun loadTanyaKandidatOrderFilter(): Single<String> {
        return Single.fromCallable {
            dataCache.loadTanyaKandidatOrderFilter()
        }
    }

    fun unVoteQuestion(id: String?, className: String): Completable {
        return apiWrapper
            .getPantauApi()
            .unVoteQuestion(
                id,
                className
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun searchTanyaKandidat(keyword: String, page: Int, perpage: Int): Single<MutableList<Pertanyaan>> {
        return apiWrapper.getPantauApi().searchTanyaKandidat(
            keyword,
            page,
            perpage,
            dataCache.loadTanyaKandidatOrderFilterSearch(),
            dataCache.loadTanyaKandidatOrderFilterDirection(),
            dataCache.loadTanyaKandidatUserFilterSearch()
        )
            .map { it.data.questions }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getUserTanyaKandidatList(page: Int, perPage: Int, userId: String): Single<TanyaKandidatListData> {
        return apiWrapper.getPantauApi().getUserTanyaKandidatList(userId, page, perPage)
            .map { it.data }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun saveTanyaKandidatFilterSearch(userFilter: String?, orderFilter: String?): Completable {
        return Completable.fromCallable { dataCache.saveTanyaKandidatUserFilterSearch(userFilter) }
            .andThen(
                Completable.fromCallable { dataCache.saveTanyaKandidatOrderFilterSearch(orderFilter) }
            )
    }

    fun loadTanyaKandidatUserFilterSearch(): Single<String> {
        return Single.fromCallable {
            dataCache.loadTanyaKandidatUserFilterSearch()
        }
    }

    fun loadTanyaKandidatOrderFilterSearch(): Single<String> {
        return Single.fromCallable {
            dataCache.loadTanyaKandidatOrderFilterSearch()
        }
    }
}