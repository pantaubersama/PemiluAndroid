package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeData
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.data.model.wordstadium.LawanDebat
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.WordsPNHandler
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class WordStadiumInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val dataCache: DataCache,
    private val rxSchedulers: RxSchedulers,
    private val wordsPNHandler: WordsPNHandler
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

    fun getPublicChallenge(progress: String, page: Int, perPage: Int = 12): Single<ChallengeData> {
        return apiWrapper.getWordStadiumApi()
            .getPublicChallenge(progress, page, perPage)
            .map { it.challengeData }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getPersonalChallenge(progress: String, page: Int, perPage: Int = 12): Single<ChallengeData> {
        return apiWrapper.getWordStadiumApi()
            .getPersonalChallenge(progress, page, perPage)
            .map { it.challengeData }
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

    fun confirmOpponentCandidate(challengeId: String, audienceId: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .confirmOpponentCandidate(challengeId, audienceId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun askAsOpponent(challengeId: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .askAsOpponent(challengeId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun approveDirect(challengeId: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .putApproveDirect(challengeId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun rejectDirect(challengeId: String, reasonRejected: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .putRejectDirect(challengeId, reasonRejected)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getChallengeItem(id: String): Single<Challenge> {
        return apiWrapper.getWordStadiumApi()
            .getChallengeItem(id)
            .map { it.challengeItemData.challenge }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getWordsFighter(challengeId: String, page: Int, perPage: Int): Single<MutableList<WordItem>> {
        return apiWrapper.getWordStadiumApi()
            .getWordsFighter(challengeId, page, perPage)
            .map { it.wordListData.wordList }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun postWordsFighter(challengeId: String, words: String): Single<WordItem> {
        return apiWrapper.getWordStadiumApi()
            .postWordsFighter(challengeId, words)
            .map { it.wordItemData.word }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getWordsAudience(challengeId: String): Single<MutableList<WordItem>> {
        return apiWrapper.getWordStadiumApi()
            .getWordsAudience(challengeId)
            .map { it.wordListData.wordList }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun postWordsAudience(challengeId: String, words: String): Single<WordItem> {
        return apiWrapper.getWordStadiumApi()
            .postWordsAudience(challengeId, words)
            .map { it.wordItemData.word }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun setOnGotNewWordsListener(onGotNewWordsListener: WordsPNHandler.OnGotNewWordsListener?) {
        wordsPNHandler.setOnGotNewWordsListener(onGotNewWordsListener)
    }

    fun handleWords(wordItem: WordItem): Completable {
        return wordsPNHandler.handleWordsPN(wordItem)
            .observeOn(rxSchedulers.io())
            .subscribeOn(rxSchedulers.mainThread())
    }

    fun putWordsClap(wordId: String): Completable {
        return apiWrapper.getWordStadiumApi()
            .putWordsClap(wordId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}