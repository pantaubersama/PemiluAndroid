package com.pantaubersama.app.data.interactors

import android.content.SharedPreferences
import com.pantaubersama.app.data.model.accesstoken.Token
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val apiWrapper: APIWrapper?,
    private val rxSchedulers: RxSchedulers?,
    private val sharedPreferences: SharedPreferences?
) {
    fun exchangeToken(oAuthToken: String?): Single<Token>? {
        return apiWrapper?.getPantauOAuthApi()?.exchangeToken(oAuthToken)
            ?.subscribeOn(rxSchedulers?.io())
            ?.observeOn(rxSchedulers?.mainThread())
    }

    fun saveLoginData(token: Token?) {
        sharedPreferences?.edit()?.putString(PantauConstants.Networking.ACCESS_TOKEN_FIELD, token?.accessToken)?.apply()
        sharedPreferences?.edit()?.putString(PantauConstants.Networking.REFRESH_TOKEN_FIELD, token?.refreshToken)?.apply()
        sharedPreferences?.edit()?.putBoolean(PantauConstants.IS_USER_LOGGED_IN, true)?.apply()
    }

    fun getLoginState(): Boolean? {
        return sharedPreferences?.getBoolean(PantauConstants.IS_USER_LOGGED_IN, false)
    }
}