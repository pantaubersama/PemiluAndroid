package com.pantaubersama.app.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by ali on 17/11/17.
 */
class ConnectionState(val context: Context) {
    fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}