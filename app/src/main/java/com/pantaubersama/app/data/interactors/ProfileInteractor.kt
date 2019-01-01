package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {

    fun refreshProfile(): Single<Profile> {
        return apiWrapper.getPantauOAuthApi().getUserProfile()
                .subscribeOn(rxSchedulers.io())
                .map { it.data.user }
                .doOnSuccess(::saveProfile)
                .observeOn(rxSchedulers.mainThread())
    }

    fun saveProfile(profile: Profile) {
        dataCache.saveUserProfile(profile)
    }

    fun getProfile(): Profile {
        return dataCache.loadUserProfile()
    }
}