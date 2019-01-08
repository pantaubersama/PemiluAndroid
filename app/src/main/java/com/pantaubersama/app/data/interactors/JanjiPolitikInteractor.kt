package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitiksData
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author edityomurti on 25/12/2018 22:15
 */
class JanjiPolitikInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val dataCache: DataCache?
) {
    fun getJanpolUserFilter(): String {
        return dataCache?.getJanpolUserFilter()!!
    }

    fun getJanpolClusterFilter(): ClusterItem? {
        return dataCache?.getJanpolClusterFilter()
    }

    fun setJanpolFilter(userFilter: String, clusterFilter: ClusterItem?) {
        dataCache?.saveJanpolUserFilter(userFilter)
        dataCache?.saveJanpolClusterFilter(clusterFilter)
    }

    fun getJanPol(
        keyword: String?,
        page: Int?,
        perPage: Int?
    ): Single<JanjiPolitiksData?>? {
        return apiWrapper.getPantauApi()
            .getJanPol(keyword, getJanpolClusterFilter()?.id ?: "", getJanpolUserFilter(), page, perPage)
            .subscribeOn(rxSchedulers.io())
            ?.map { it.data }
            ?.observeOn(rxSchedulers.mainThread())
    }

    fun createJanjiPolitik(title: RequestBody, body: RequestBody, image: MultipartBody.Part?): Single<JanjiPolitik> {
        return apiWrapper.getPantauApi().createJanjiPolitik(title, body, image)
            .subscribeOn(rxSchedulers.io())
            .map { it.data.janjiPolitik!! }
            .observeOn(rxSchedulers.mainThread())
    }
}