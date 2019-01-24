package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.AchievedBadge
import com.pantaubersama.app.data.model.user.Informant
import com.pantaubersama.app.data.model.user.ProfileResponse
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.data.remote.exception.ErrorException
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

    fun getAchievedBadgeByID(achievedId: String): Single<AchievedBadge> {
        return apiWrapper.getPantauOAuthApi().getAchievedBadgeById(achievedId)
                .subscribeOn(rxSchedulers.io())
                .map { it.data.achievedBadge }
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

    fun getDataLapor(): Single<Informant> {
//        return apiWrapper
//            .getPantauOAuthApi()
//            .getDataLapor()
//            .subscribeOn(rxSchedulers.io())
//            .observeOn(rxSchedulers.mainThread())
        return Single.fromCallable {
            dataCache.loadUserProfile().informant
        }
    }

    fun updateDataLapor(
        idNumber: String?,
        pob: String?,
        dob: String?,
        gender: Int?,
        occupation: String?,
        nationality: String?,
        address: String?,
        phoneNumber: String?
    ): Completable {
        return apiWrapper
                .getPantauOAuthApi()
                .updateDataLapor(
                        idNumber,
                        pob,
                        dob,
                        gender,
                        occupation,
                        nationality,
                        address,
                        phoneNumber
                )
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .doOnComplete {
                    val newProfile = dataCache.loadUserProfile()
                    newProfile.informant.identityNumber = idNumber
                    newProfile.informant.pob = pob
                    newProfile.informant.dob = dob
                    newProfile.informant.gender = gender
                    newProfile.informant.occupation = occupation
                    newProfile.informant.nationality = nationality
                    newProfile.informant.address = address
                    newProfile.informant.phoneNumber = phoneNumber
                    dataCache.saveUserProfile(newProfile)
                }
    }

    fun isModerator(): Boolean {
        return getProfile().isModerator
    }

    fun isEligible(): Boolean {
        return if (getProfile().cluster == null) {
            false
        } else {
            getProfile().cluster?.isEligible!!
        }
    }

    fun usernameCheck(username: String?): Single<ProfileResponse> {
        return apiWrapper
                .getPantauOAuthApi()
                .usernameCheck(username)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun submitCatatanku(paslonSelected: Int, partySelected: String): Completable {
        return apiWrapper
                .getPantauOAuthApi()
                .submitCatatanku(paslonSelected, partySelected)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .doOnComplete {
                    val newProfile = dataCache.loadUserProfile()
                    newProfile.votePreference = paslonSelected
                    newProfile.politicalParty?.id = partySelected
                    dataCache.saveUserProfile(newProfile)
                }
    }

    fun getMyTendency(): Single<KuisUserResult> {
        return apiWrapper.getPantauApi().getKuisUserResult()
                .map { response ->
                    val team = response.data.teams.maxBy { it.percentage }
                    team?.let { KuisUserResult(it.percentage, it.team, response.data.meta.quizzes, response.data.user) }
                            ?: throw ErrorException("Gagal mendapatkan hasil kuis")
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun getPartai(page: Int, perPage: Int): Single<List<PoliticalParty>> {
        return apiWrapper.getPantauOAuthApi().getPartai(page, perPage)
                .subscribeOn(rxSchedulers.io())
                .map { response ->
                    response.data.politicalParties
                }
                .observeOn(rxSchedulers.mainThread())
    }
}