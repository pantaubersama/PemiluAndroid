package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.AppVersion
import com.pantaubersama.app.data.model.AppVersionData
import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.model.accesstoken.TokenResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.services.AccountService
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {
    fun exchangeToken(oAuthToken: String?): Single<TokenResponse>? {
        return apiWrapper.getPantauOAuthApi().exchangeToken(oAuthToken)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun saveLoginData(token: Token?) {
        dataCache.saveToken(token?.accessToken!!)
        dataCache.saveRefreshToken(token.refreshToken!!)
        dataCache.saveLoginState(true)
    }

    fun getLoginState(): Boolean? {
        return dataCache.loadLoginState()
    }

    fun logOut(clientId: String?, clientSecret: String?): Completable? {
        return apiWrapper.getPantauOAuthApi()
            .revokeToken(clientId, clientSecret)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun clearDataCache() {
        dataCache.clear()
    }

    fun connectFacebook(accountType: String, token: String?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .connectSocialMedia(accountType, token)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun connectTwitter(accountType: String, token: String, secret: String): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .connectTwitter(accountType, token, secret)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getTwitterAccountService(): AccountService {
        return TwitterCore.getInstance().apiClient.accountService
    }

    fun disconnectSocielMedia(accountType: String): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .disconnectSocialMedia(accountType)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun isForceUpdateAvalable(): Single<AppVersion> {
        return apiWrapper.getPantauApi().getLatestAppVersion()
            .subscribeOn(rxSchedulers.io())
            .map { it.data.appVersion }
            .observeOn(rxSchedulers.mainThread())
    }
}