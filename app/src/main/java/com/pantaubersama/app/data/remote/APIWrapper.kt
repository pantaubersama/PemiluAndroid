package com.pantaubersama.app.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

class APIWrapper @Inject constructor(private val pantauAPI: PantauAPI, private val pantauOAuthAPI: PantauOAuthAPI, private val wordStadiumAPI: WordStadiumAPI) {

    fun getPantauApi(): PantauAPI {
        return pantauAPI
    }

    fun getPantauOAuthApi(): PantauOAuthAPI {
        return pantauOAuthAPI
    }

    fun getWordStadiumApi(): WordStadiumAPI {
        return wordStadiumAPI
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