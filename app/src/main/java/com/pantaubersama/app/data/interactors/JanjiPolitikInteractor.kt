package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitiksData
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:15
 */
class JanjiPolitikInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache
) {
    fun getJanpolUserFilter(): String {
        return dataCache.getJanpolUserFilter()
    }

    fun getSearchJanpolUserFilter(): String {
        return dataCache.getSearchJanpolUserFilter()
    }

    fun getJanpolClusterFilter(): ClusterItem? {
        return dataCache.getJanpolClusterFilter()
    }

    fun getSearchJanpolClusterFilter(): ClusterItem? {
        return dataCache.getSearchJanpolClusterFilter()
    }

    fun setJanpolFilter(userFilter: String, clusterFilter: ClusterItem?) {
        dataCache.saveJanpolUserFilter(userFilter)
        dataCache.saveJanpolClusterFilter(clusterFilter)
    }

    fun setSearchJanpolFilter(userFilter: String, clusterFilter: ClusterItem?) {
        dataCache.saveSearchJanpolUserFilter(userFilter)
        dataCache.saveSearchJanpolClusterFilter(clusterFilter)
    }

    fun getJanPolById(janpolId: String): Single<JanjiPolitik> {
        return apiWrapper.getPantauApi()
            .getJanjiPolitik(janpolId)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.janjiPolitik!! }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getJanPol(
        keyword: String?,
        page: Int?,
        perPage: Int?,
        clusterId: String,
        userType: String
    ): Single<JanjiPolitiksData?> {
        return apiWrapper.getPantauApi()
            .getJanjiPolitikList(keyword, clusterId, userType, page, perPage)
            .subscribeOn(rxSchedulers.io())
            .map { it.data }
            .observeOn(rxSchedulers.mainThread())
    }

    fun getMyJanPol(keyword: String, page: Int?, perPage: Int): Single<JanjiPolitiksData?> {
        return apiWrapper.getPantauApi()
            .getMyJanjiPolitikList(keyword, page, perPage)
            .subscribeOn(rxSchedulers.io())
            .map { it.data }
            .observeOn(rxSchedulers.mainThread())
    }

    fun createJanjiPolitik(title: RequestBody, body: RequestBody, image: MultipartBody.Part?): Single<JanjiPolitik> {
        return apiWrapper.getPantauApi().createJanjiPolitik(title, body, image)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.janjiPolitik!! }
            .observeOn(rxSchedulers.mainThread())
    }

    fun deleteJanjiPolitik(id: String): Completable {
        return apiWrapper.getPantauApi().deleteJanjiPolitik(id)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getUserJanpol(userId: String, page: Int, perPage: Int): Single<MutableList<JanjiPolitik>?> {
        return apiWrapper.getPantauApi().getUserJanpul(userId, page, perPage)
            .map { it.data?.janjiPolitikList }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}