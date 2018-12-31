package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.data.model.user.UserResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {

    fun refreshProfile(): Single<UserResponse> {
        return apiWrapper.getPantauOAuthApi().getUserProfile()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun saveProfile(profile: User) {
        dataCache.saveUserProfile(profile)
    }

    fun getProfile(): User {
        return dataCache.loadUserProfile()
    }
}