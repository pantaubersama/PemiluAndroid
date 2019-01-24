package com.pantaubersama.app.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.data.remote.exception.ErrorNotConnectException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.remote.exception.ErrorTimeoutException
import retrofit2.HttpException

class NetworkErrorInterceptor(
    private val connectionState: ConnectionState
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        try {
            val response = chain.proceed(chain.request())
            if (!connectionState.isConnected()) throw ErrorNotConnectException()
            if (!response.isSuccessful) {
                throw ErrorException(response.code(), parseErrorResponse(response.body()!!.string()))
            } else {
            }

            return response
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (e is UnknownHostException) throw ErrorNotConnectException()
            if (e is SocketTimeoutException) throw ErrorTimeoutException()
//            if (e is ErrorInvalidUserException) throw ErrorInvalidUserException()
            if (e is ErrorException && e.getCode() > -1)
                throw ErrorException(e.message!!)
            if (e is HttpException)
                throw Throwable("Error!! HttpException code: ${e.code()}")
            throw ErrorException()
        }
    }

    private fun parseErrorResponse(message: String): String {
        val jsonObject = Gson().fromJson(message, JsonObject::class.java)
        if (jsonObject.has("error")) {
            val jsonError = jsonObject.getAsJsonObject("error")
            if (jsonError.has("errors")) {

                return jsonError.getAsJsonArray("errors").asJsonArray.get(0).asString
            }
        }
        return "oops, terjadi kesalahan jaringan" // Unkown error
    }
}