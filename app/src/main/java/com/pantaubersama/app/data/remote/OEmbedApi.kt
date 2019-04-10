package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OEmbedApi {
    @GET("oembed")
    fun getConvertLink(
        @Query("url") url: String
    ): Single<OEmbedLink>
}