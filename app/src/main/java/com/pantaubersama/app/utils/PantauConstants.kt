package com.pantaubersama.app.utils

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {

    companion object {
        const val LABEL_COPY = "LABEL_COPY"

        const val BANNER_PILPRES = "pilpres"
        const val BANNER_JANPOL = "janji politik"
        const val BANNER_TANYA = "tanya"
        const val BANNER_KUIS = "kuis"
    }

    object TanyaKandidat {
        const val CREATE_TANYA_KANDIDAT_REQUEST_CODE = 21
        val TANYA_KANDIDAT_DATA = "tanya_kandidat_data"
        val CLASS_NAME = "Question"
        object Filter {
            const val FILTER_TANYA_KANDIDAT_REQUEST_CODE = 22
            object ByVerified {
                const val USER_VERIFIED_ALL = "user_verified_all"
                const val USER_VERIFIED_TRUE = "user_verified_true"
                const val USER_VERIFIED_FALSE = "user_verified_false"
            }
            object ByVotes {
                const val LATEST = "created"
                const val MOST_VOTES = "cached_votes_up"
            }
        }
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
        const val BANNER_PILPRES = 101
        const val BANNER_JANPOL = 102
        const val BANNER_TANYA_KANDIDAT = 103
        const val BANNER_KUIS = 104
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

        const val BASE_TWEET_URL = "https://twitter.com/i/web/status/"
    }

    object Kuis {
        const val KUIS_ID = "kuis_id"
        const val KUIS_NUMBER = "kuis_number"
    }

    object Camera {
        val ASK_PERMISSIONS_REQUEST_CODE = 123
        val BITMAP = "bitmap"
    }

    object ItemModel {
        const val TYPE_LOADING = 0
        const val TYPE_BANNER = 1
        const val TYPE_FEEDS = 2
    }
}