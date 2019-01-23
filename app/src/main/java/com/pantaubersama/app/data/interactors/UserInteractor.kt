package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val dataCache: DataCache,
    private val rxSchedulers: RxSchedulers
) {
    fun getSearchOrangFilter(): String {
        return dataCache.getSearchOrangFilter()
    }

    fun saveSearchOrangFilter(searchOrangFilter: String): Completable {
        return Completable.fromCallable { dataCache.saveTanyaKandidatUserFilter(searchOrangFilter) }
    }

    fun searchPerson(keyword: String, page: Int, perPage: Int): Single<MutableList<Profile>> {
        return apiWrapper
            .getPantauOAuthApi()
            .searchUser(
                keyword,
                page,
                perPage,
                dataCache.getSearchOrangFilter()
            )
            .map { it.data.users }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getUserProfile(userId: String): Single<Profile> {
        return apiWrapper.getPantauOAuthApi().getUserProfile(userId)
            .map { it.data.user }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getBadges(userId: String): Single<List<Badge>> {
        return apiWrapper.getPantauOAuthApi().getUserBadges(userId)
            .subscribeOn(rxSchedulers.io())
            .map { response ->
                val achievedBadges = response.data.achievedBadges
                    .map {
                        it.badge.apply {
                            achievedId = it.achievedId
                            achieved = true
                        }
                    }
                achievedBadges + response.data.badges
            }
            .observeOn(rxSchedulers.mainThread())
    }
}