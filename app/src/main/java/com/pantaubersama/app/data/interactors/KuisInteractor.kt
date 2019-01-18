package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.kuis.* // ktlint-disable
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.remote.PantauAPI
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.utils.PantauConstants.Kuis.Filter
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

    fun getKuisList(page: Int, perPage: Int, filterBy: String): Single<List<KuisItem>> {
        return if (dataCache.loadUserProfile() != EMPTY_PROFILE) {
            when (filterBy) {
                Filter.BELUM_SELESAI -> pantauAPI.getKuisInProgress(page, perPage)
                Filter.SELESAI -> pantauAPI.getKuisFinished(page, perPage)
                else -> pantauAPI.getKuisNotParticipating(page, perPage)
            }
        } else {
            pantauAPI.getKuisNotParticipating(page, perPage)
        }
            .subscribeOn(rxSchedulers.io())
            .map { it.data.kuisList }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisById(quizId: String): Single<KuisItem> {
        return pantauAPI.getKuisById(quizId)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.kuisItem }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisUserSummary(): Single<KuisUserResult> {
        return pantauAPI.getKuisUserResult()
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val team = response.data.teams.maxBy { it.percentage }
                team?.let { KuisUserResult(it.percentage, it.team, response.data.meta.quizzes, response.data.user) }
                    ?: throw ErrorException("Gagal mendapatkan hasil kuis")
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisUserSummaryByUserId(userId: String): Single<KuisUserResult> {
        return pantauAPI.getKuisUserResultByUserId(userId)
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val team = response.data.teams.maxBy { it.percentage }
                team?.let { KuisUserResult(it.percentage, it.team, response.data.meta.quizzes, response.data.user) }
                ?: throw ErrorException("Gagal mendapatkan hasil kuis")
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisResult(kuisId: String): Single<KuisResult> {
        return pantauAPI.getKuisResult(kuisId)
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val team = response.data.teams.maxBy { it.percentage }
                team?.let { KuisResult(it.percentage, it.team, response.data.user, response.data.quizParticipation, response.data.quiz.title) }
                    ?: throw ErrorException("Gagal mendapatkan hasil kuis")
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisResultByQuizParticipationId(quizParticipationId: String): Single<KuisResult> {
        return pantauAPI.getKuisResultByQuizParticipationId(quizParticipationId)
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val team = response.data.teams.maxBy { it.percentage }
                team?.let { KuisResult(it.percentage, it.team, response.data.user, response.data.quizParticipation, response.data.quiz.title) }
                    ?: throw ErrorException("Gagal mendapatkan hasil kuis")
            }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getKuisFilter(): String {
        return dataCache.getKuisFilter()
    }

    fun saveKuisFilter(kuisFilter: String): Completable {
        return Completable.fromCallable {
            dataCache.saveKuisFilter(kuisFilter)
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

    fun getKuisSummary(kuisId: String): Single<List<AnsweredQuestion>> {
        return pantauAPI.getKuisSummary(kuisId)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.questions }
            .observeOn(rxSchedulers.mainThread())
    }
}