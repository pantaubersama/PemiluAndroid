package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
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

    fun updateUserData(
        name: String?,
        username: String?,
        location: String?,
        description: String?,
        education: String?,
        occupation: String?
    ): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .updateUserData(
                name,
                username,
                location,
                description,
                education,
                occupation
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnComplete {
                val newProfile = dataCache.loadUserProfile()
                newProfile.name = name!!
                newProfile.username = username!!
                newProfile.location = location!!
                newProfile.about = description!!
                newProfile.education = education!!
                newProfile.occupation = occupation!!
                dataCache.saveUserProfile(newProfile)
            }
    }

    fun uploadAvatar(avatar: MultipartBody.Part?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .uploadAvatar(avatar)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun updatePassword(password: String, confirmation: String): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .updatePassword(password, confirmation)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}