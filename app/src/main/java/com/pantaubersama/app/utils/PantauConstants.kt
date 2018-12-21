package com.pantaubersama.app.utils

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {
    companion object {
        const val IS_USER_LOGGED_IN = "is_user_logged_in"
    }

    object Extra {
        const val SELECTED_FILTER = "EXTRA_SELECTED_FILTER"
    }

    object RequestCode {
        const val FILTER_PILPRES = 1001
    }

    object Networking {
        const val AUTHORIZATION = "Authorization"
        const val GRANT_TYPE = "refresh_token"
        const val ACCESS_TOKEN_FIELD = "client_token"
        const val REFRESH_TOKEN_FIELD = "client_refresh_token"
        const val OAUTH_ACCESS_TOKEN_FIELD = "access_token"
    }
}