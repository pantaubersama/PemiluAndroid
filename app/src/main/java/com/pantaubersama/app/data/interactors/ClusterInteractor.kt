package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.data.model.cluster.CategoryData
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class ClusterInteractor @Inject constructor(
    private val dataCache: DataCache,
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun getClusterList(page: Int, perPage: Int, keyword: String, categoryId: String): Single<MutableList<ClusterItem>> {
        return apiWrapper.getPantauOAuthApi()
            .getClusterList(page, perPage, keyword, categoryId)
            .subscribeOn(rxSchedulers.io())
            .map { it.clustersData.clusterList }
            .map {
                val eligibleCluster: MutableList<ClusterItem> = ArrayList()
                for (cluster in it) {
                    if (cluster.isEligible) {
                        eligibleCluster.add(cluster)
                    }
                }
                eligibleCluster
            }
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

    fun getSearchClusterFilter(): Category? {
        return dataCache.getFilterSearchClusterCategory()
    }

    fun setSearchClusterFilter(categoryFilter: Category?) {
        dataCache.saveFilterSearchClusterCategory(categoryFilter)
    }

    fun getClusterById(clusterId: String): Single<ClusterItem> {
        return apiWrapper.getPantauOAuthApi().getClusterById(clusterId)
            .subscribeOn(rxSchedulers.io())
            .map { it.clustersData.cluster }
            .observeOn(rxSchedulers.mainThread())
    }
}