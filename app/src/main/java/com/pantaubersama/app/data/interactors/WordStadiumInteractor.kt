package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class WordStadiumInteractor @Inject constructor(
        private val apiWrapper: APIWrapper,
        private val rxSchedulers: RxSchedulers
) {
    fun openChallenge(
            token: String?,
            topicList: String?,
            statement: String?,
            statementSource: String?,
            showTimeAt: String?,
            timeLimit: Int
    ): Completable {
        return apiWrapper
                .getWordStadiumApi()
                .openChallenge(
                        token,
                        topicList,
                        statement,
                        statementSource,
                        showTimeAt,
                        timeLimit
                )
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }
}