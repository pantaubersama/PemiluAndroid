package com.pantaubersama.app.data.remote

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauAPI {
    @FormUrlEncoded
    @POST("pendidikan_politik/v1/questions")
    fun createTanyaKandidat(@Field("body") body: String?): Single<ResponseBody>
}