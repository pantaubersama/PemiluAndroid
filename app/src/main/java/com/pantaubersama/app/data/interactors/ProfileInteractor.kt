package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
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

    fun getBadges(): Single<List<Badge>> {
        return apiWrapper.getPantauOAuthApi().getUserBadges()
                .subscribeOn(rxSchedulers.io())
                .map { response ->
                    response.data.achievedBadges.apply { forEach { it.achieved = true } } +
                            response.data.badges
                }
                .observeOn(rxSchedulers.mainThread())
    }

    fun leaveCluster(): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .leaveCluster()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}