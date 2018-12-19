package com.pantaubersama.app.utils

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {
    companion object {
        val IS_USER_LOGGED_IN = "is_user_logged_in"
    }

    object Networking {
        val AUTHORIZATION = "Authorization"
        val GRANT_TYPE = "refresh_token"
        val ACCESS_TOKEN_FIELD = "client_token"
        val REFRESH_TOKEN_FIELD = "client_refresh_token"
        val OAUTH_ACCESS_TOKEN_FIELD = "access_token"
    }
}