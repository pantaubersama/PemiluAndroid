package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.tags.TagsResponse
import com.pantaubersama.app.data.model.urlpreview.UrlItemResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author edityomurti on 20/03/2019 13:48
 */
interface OpiniumServiceAPI {
    @GET("common/v1/crawl")
    fun getUrlMeta(@Query("url") url: String): Single<UrlItemResponse>

    @GET("opinium_service/v1/tags")
    fun getTags(@Query("page") page: Int,
                @Query("per_page") perPage: Int,
                @Query("q") keyword: String): Single<TagsResponse>
}