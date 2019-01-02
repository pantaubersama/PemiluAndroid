package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatResponse
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
    fun isBannerShown(): Boolean? {
        return dataCache.isBannerTanyaKandidatOpened()
    }

    fun createTanyaKandidat(body: String?): Single<TanyaKandidatResponse> {
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
        perPage: Int?,
        orderBy: String?,
        direction: String?,
        filterBy: String?
    ): Single<TanyaKandidatResponse> {
        return apiWrapper.getPantauApi().getTanyaKandidatList(
            page,
            perPage,
            orderBy,
            direction,
            filterBy
        )
            .subscribeOn(rxSchedulers.io())
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
        return Completable.fromCallable { dataCache.saveTanyaKandidatUserFilter(userFilter!!) }
            .andThen(
                Completable.fromCallable { dataCache.saveTanyaKandidatOrderFilter(orderFilter!!) }
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
}