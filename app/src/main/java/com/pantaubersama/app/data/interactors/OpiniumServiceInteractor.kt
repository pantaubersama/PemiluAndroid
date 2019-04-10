package com.pantaubersama.app.data.interactors

import com.pantaubersama.app.data.model.tags.TagItem
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author edityomurti on 20/03/2019 14:04
 */
class OpiniumServiceInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers
) {
    fun getUrlMeta(url: String): Single<UrlItem> {
        return apiWrapper.getOpiniumServiceApi()
            .getUrlMeta(url)
            .map { it.data.url }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }

    fun getTags(page: Int, perPage: Int, keyword: String): Single<MutableList<TagItem>> {
        return apiWrapper.getOpiniumServiceApi()
            .getTags(page, perPage, keyword)
            .map { it.tagsData.tags }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
    }
}