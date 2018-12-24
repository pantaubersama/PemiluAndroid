package com.pantaubersama.app.utils

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {

    object Extra {
        const val SELECTED_FILTER_PILPRES = "EXTRA_SELECTED_FILTER"
        const val BANNER_INFO_TYPE = "EXTRA_BANNER_INFO_TYPE"
        const val TYPE_PILPRES = 0
        const val TYPE_JANPOL = 1
        const val TYPE_TANYA_KANDIDAT = 2
        const val TYPE_KUIS = 3
    }

    object RequestCode {
        const val FILTER_PILPRES = 1001
    }

    object Networking {
        const val AUTHORIZATION = "Authorization"
        const val GRANT_TYPE = "refresh_token"
        const val OAUTH_ACCESS_TOKEN_FIELD = "access_token"
    }

    object Kuis {
        const val KUIS_ID = "kuis_id"
    }
}