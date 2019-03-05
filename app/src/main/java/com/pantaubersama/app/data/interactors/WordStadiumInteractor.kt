package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class WordStadiumInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
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

    fun confirmOpponentCandidate(challengeId: String, audienceId: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .confirmOpponentCandidate(challengeId, audienceId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}