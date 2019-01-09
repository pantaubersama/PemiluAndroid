package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisQuestions
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

    fun getKuisList(page: Int, perPage: Int): Single<List<KuisItem>> {
        return pantauAPI.getKuisList(page, perPage)
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

    fun getKuisQuestions(kuisId: String): Single<KuisQuestions> {
        return pantauAPI.getKuisQuestions(kuisId)
            .subscribeOn(rxSchedulers.io())
            .map {
                KuisQuestions(it.data.questions, it.data.meta.quizzes.answeredQuestionsCount,
                    it.data.meta.quizzes.quizQuestionsCount)
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun answerQuestion(kuisId: String, questionId: String, answerId: String): Completable {
        return pantauAPI.answerQuestion(kuisId, questionId, answerId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}