package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.wordstadium.LawanDebat
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class WordStadiumInteractor @Inject constructor(
        private val apiWrapper: APIWrapper,
        private val dataCache: DataCache,
        private val rxSchedulers: RxSchedulers
) {
    fun openChallenge(
        topicList: String?,
        statement: String?,
        statementSource: String?,
        showTimeAt: String?,
        timeLimit: Int
    ): Completable {
        return apiWrapper
            .getWordStadiumApi()
            .openChallenge(
                topicList,
                statement,
                statementSource,
                showTimeAt,
                timeLimit
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun directChallenge(
            topicList: String?,
            statement: String?,
            statementSource: String?,
            showTimeAt: String?,
            timeLimit: Int,
            invitationId: String?,
            screenName: String?
    ): Completable {
        return apiWrapper
                .getWordStadiumApi()
                .directChallenge(
                        topicList,
                        statement,
                        statementSource,
                        showTimeAt,
                        timeLimit,
                        invitationId,
                        screenName
                )
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun getConvertLink(url: String): Single<OEmbedLink> {
        return apiWrapper
            .getOEmbedApi()
            .getConvertLink(url)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getPublicChallenge(progress: String): Single<List<Challenge>> {
        return apiWrapper.getWordStadiumApi()
            .getPublicChallenge(progress)
            .map { it.challengeData.challenges }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getPersonalChallenge(progress: String): Single<List<Challenge>> {
        return apiWrapper.getWordStadiumApi()
            .getPersonalChallenge(progress)
            .map { it.challengeData.challenges }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun searchLawanDebat(keyword: String, page: Int, perPage: Int): Single<MutableList<LawanDebat>> {
        return apiWrapper
                .getPantauApi()
                .getLawanDebatTwitter(
                        keyword,
                        page,
                        perPage
                )
                .map { it.data.users }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun searchPerson(keyword: String, page: Int, perPage: Int): Single<MutableList<LawanDebat>> {
        return apiWrapper
                .getPantauOAuthApi()
                .searchPerson(
                        keyword,
                        page,
                        perPage,
                        dataCache.getSearchOrangFilter()
                )
                .map { it.data.users }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }
}