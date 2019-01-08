package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.remote.PantauAPI
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class KuisInteractor @Inject constructor(
    private val pantauAPI: PantauAPI,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {
    fun isBannerShown(): Boolean? {
        return dataCache.isBannerKuisOpened()
    }

    fun getKuisList(page: Int): Single<List<KuisItem>> {
        return pantauAPI.getKuisList(page)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.kuisList }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisUserSummary(): Single<KuisUserResult> {
        return pantauAPI.getKuisUserResult()
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val team = response.data.teams.maxBy { it.percentage }
                team?.let { KuisUserResult(it.percentage, it.team, response.data.meta.quizzes) }
                    ?: throw ErrorException("Gagal mendapatkan hasil kuis")
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisFilter(): String? {
        return dataCache.getKuisFilter()
    }

    fun saveKuisFilter(kuisFilter: String?): Completable {
        return Completable.fromCallable {
            dataCache.saveKuisFilter(kuisFilter!!)
        }
    }
}