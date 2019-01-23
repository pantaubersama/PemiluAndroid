package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.cluster.CategoryData
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class ClusterInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun getClusterList(page: Int, perPage: Int): Single<MutableList<ClusterItem>>? {
        return apiWrapper.getPantauOAuthApi()
            .getClusterList(page, perPage)
            .subscribeOn(rxSchedulers.io())
            .map { it.clustersData.clusterList }
            .observeOn(rxSchedulers.mainThread())
    }

    fun createNewCategory(categoryName: String): Single<CategoryData> {
        return apiWrapper.getPantauOAuthApi().createNewCategory(categoryName)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getCategories(page: Int, perPage: Int, query: String): Single<CategoryData> {
        return apiWrapper
            .getPantauOAuthApi()
            .getCategories(page, perPage, query)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun requestCluster(clusterName: String, categoryId: String, clusterDescription: String, image: MultipartBody.Part?): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .requestCluster(
                clusterName,
                categoryId,
                clusterDescription,
                image
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun invite(email: String, clusterId: String): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .inviteToCluster(email, clusterId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun enableDisableUlInvite(clusterId: String, disable: Boolean): Completable {
        return apiWrapper
            .getPantauOAuthApi()
            .enableDisableCluster(
                clusterId,
                disable
            )
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}