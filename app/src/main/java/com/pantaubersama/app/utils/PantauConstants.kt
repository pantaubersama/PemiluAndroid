package com.pantaubersama.app.utils

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {

    companion object {
        const val LABEL_COPY = "LABEL_COPY"
    }

    object TanyaKandidat {
        const val CREATE_TANYA_KANDIDAT_REQUEST_CODE = 21
        val TANYA_KANDIDAT_DATA = "tanya_kandidat_data"
    }

    object Extra {
        const val SELECTED_FILTER_PILPRES = "EXTRA_SELECTED_FILTER"
        const val BANNER_INFO_TYPE = "EXTRA_BANNER_INFO_TYPE"
        const val TYPE_PILPRES = 0
        const val TYPE_JANPOL = 1
        const val TYPE_TANYA_KANDIDAT = 2
        const val TYPE_KUIS = 3
        const val JANPOL_ID = "JANPOL_ID"
    }

    object RequestCode {
        const val FILTER_PILPRES = 1001
        const val FILTER_JANPOL = 1002
        const val FILTER_TANYA_KANDIDAT = 1003
        const val FILTER_KUIS = 1004
    }

    object Networking {
        const val AUTHORIZATION = "Authorization"
        const val GRANT_TYPE = "refresh_token"
        const val OAUTH_ACCESS_TOKEN_FIELD = "access_token"
        val NON_TOKEN_URL = arrayOf(
            "/v1/callback",
            "/oauth/token"
        )
        val OPTIONAL_TOKEN_URL = arrayOf(
            "/v1/profile"
        )
        val BEARER = "bearer "
    }

    object Kuis {
        const val KUIS_ID = "kuis_id"
        const val KUIS_NUMBER = "kuis_number"
    }

    object Camera {
        val ASK_PERMISSIONS_REQUEST_CODE = 123
        val BITMAP = "bitmap"
    }
}