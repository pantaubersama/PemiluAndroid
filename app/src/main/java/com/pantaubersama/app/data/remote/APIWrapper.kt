package com.pantaubersama.app.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class APIWrapper @Inject constructor(
     private val pantauAPI: PantauAPI,
     private val pantauOAuthAPI: PantauOAuthAPI,
     private val wordStadiumAPI: WordStadiumAPI,
     private val oembedAPI: OEmbedApi,
     private val opiniumServiceAPI: OpiniumServiceAPI
) {

    fun getPantauApi(): PantauAPI {
        return pantauAPI
    }

    fun getPantauOAuthApi(): PantauOAuthAPI {
        return pantauOAuthAPI
    }

    fun getWordStadiumApi(): WordStadiumAPI {
        return wordStadiumAPI
    }

    fun getOEmbedApi(): OEmbedApi {
        return oembedAPI
    }

    fun getOpiniumServiceApi(): OpiniumServiceAPI {
        return opiniumServiceAPI
    }

    companion object {
        fun createRetrofit(baseUrl: String, httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}