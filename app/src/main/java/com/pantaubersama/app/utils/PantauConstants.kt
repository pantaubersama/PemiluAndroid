package com.pantaubersama.app.utils

import android.Manifest

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

    object Cluster {
        const val CATEGORY = "category"
    }

    object TanyaKandidat {
        const val CREATE_TANYA_KANDIDAT_REQUEST_CODE = 21
        val TANYA_KANDIDAT_DATA = "tanya_kandidat_data"
        val CLASS_NAME = "Question"
        object Filter {
            const val FILTER_TANYA_KANDIDAT_REQUEST_CODE = 22
            val FILTER_ORDER_DIRECTION = "filter_order_direction"

            object ByVerified {
                const val USER_VERIFIED_ALL = "user_verified_all"
                const val USER_VERIFIED_TRUE = "user_verified_true"
                const val USER_VERIFIED_FALSE = "user_verified_false"
            }
            object ByVotes {
                const val LATEST = "created_at"
                const val MOST_VOTES = "cached_votes_up"
            }
        }
    }

    object Extra {
        const val EXTRA_SELECTED_FILTER_PILPRES = "EXTRA_SELECTED_FILTER"
        const val EXTRA_BANNER_INFO_TYPE = "EXTRA_BANNER_INFO_TYPE"
        const val EXTRA_BANNER_INFO_DATA = "EXTRA_BANNER_INFO_DATA"
        const val EXTRA_TYPE_PILPRES = 0
        const val EXTRA_TYPE_JANPOL = 1
        const val EXTRA_TYPE_TANYA_KANDIDAT = 2
        const val EXTRA_TYPE_KUIS = 3
        const val EXTRA_JANPOL_ID = "EXTRA_JANPOL_ID"
        const val EXTRA_JANPOL_ITEM = "EXTRA_JANPOL_ITEM"
        const val EXTRA_ITEM_POSITION = "EXTRA_ITEM_POSITION"
    }

    object RequestCode {
        const val RC_FILTER_PILPRES = 1001
        const val RC_FILTER_JANPOL = 1002
        const val RC_FILTER_TANYA_KANDIDAT = 1003
        const val RC_REFRESH_KUIS_ON_RESULT = 1004
        const val RC_BANNER_PILPRES = 101
        const val RC_BANNER_JANPOL = 102
        const val RC_BANNER_TANYA_KANDIDAT = 103
        const val RC_BANNER_KUIS = 104
        const val RC_ASK_PERMISSIONS = 115
        const val RC_CAMERA = 111
        const val RC_STORAGE = 112
        const val RC_SETTINGS = 600
        const val RC_CREATE_JANPOL = 1010
        const val RC_OPEN_DETAIL_JANPOL = 1011
    }

    object ResultCode {
        const val RESULT_DELETE_ITEM_JANPOL = 111
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
        const val URL_PANTAU_BERSAMA = "www.pantaubersama.com"
        const val URL_PANTAU_BERSAMA_TWITTER = "https://twitter.com/pantaubersama"
        const val URL_PANTAU_BERSAMA_INSTAGRAM = "https://www.instagram.com/pantaubersama/"
        const val URL_PANTAU_BERSAMA_FACEBOOK = "https://www.facebook.com/pages/category/Internet-Company/Pantau-Bersama-735930099884846/"
    }

    object Kuis {
        const val KUIS_ID = "kuis_id"
        const val KUIS_ITEM = "kuis_item"
        const val KUIS_TITLE = "kuis_title"
        const val KUIS_FILTER = "kuis_filter"
        const val KUIS_REFRESH = "kuis_refresh"

        object Filter {
            const val KUIS_ALL = "all"
            const val BELUM_DIIKUTI = "not_participating"
            const val BELUM_SELESAI = "in_progress"
            const val SELESAI = "finished"
        }
    }

    object Camera {
        val ASK_PERMISSIONS_REQUEST_CODE = 123
        val BITMAP = "bitmap"
    }

    object ItemModel {
        const val TYPE_LOADING = 0
        const val TYPE_BANNER = 1
        const val TYPE_FEEDS = 2
        const val TYPE_JANPOL = 3
        const val TYPE_PERTANYAAN = 4
        const val TYPE_KUIS_RESULT = 5
        const val TYPE_KUIS_ITEM = 6
        const val TYPE_CLUSTER_ITEM = 7
        val CATEGORY_ITEM = 8
    }
    object Profile

    object Filter {
        object Pilpres { // value dari backend
            const val FILTER_ALL = "team_all"
            const val FILTER_TEAM_1 = "team_id_1"
            const val FILTER_TEAM_2 = "team_id_2"
        }

        object Janpol { // value dari backend
            const val USER_VERIFIED_ALL = "user_verified_all"
            const val USER_VERIFIED_TRUE = "user_verified_true"
            const val USER_VERIFIED_FALSE = "user_verified_false"
        }
    }

    object Regex {
        const val KTP =
            "^((1[1-9])|(21)|([37][1-6])|(5[1-4])|(6[1-5])|([8-9][1-2]))[0-9]{2}[0-9]{2}(([0-6][0-9])|(7[0-1]))((0[1-9])|(1[0-2]))([0-9]{2})[0-9]{4}\$"
    }

    object Permission {
        val GET_IMAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}