package com.pantaubersama.app.utils

import android.Manifest

/**
 * @author edityomurti on 14/12/2018 14:54
 */
class PantauConstants {

    object Search {
        object Filter {
            const val SEARCH_ORANG_REQUEST_CODE = 1111
        }
    }

    companion object {
        const val LABEL_COPY = "LABEL_COPY"

        const val BANNER_PILPRES = "pilpres"
        const val BANNER_JANPOL = "janji politik"
        const val BANNER_TANYA = "tanya"
        const val BANNER_KUIS = "kuis"
        const val URL = "url"
        val PROFILE_COMPLETION = "profile_completion"
        val FILTER_ORANG_ALL = "verified_all"
        val FILTER_ORANG_VERIFIED = "verified_true"
        val FILTER_ORANG_UNVERIFIED = "verified_false"
        val CONFIRMATION_PATH = "confirmation"
    }

    object Cluster {
        const val CATEGORY = "category"
        val CLUSTER_URL = "cluster_url"
        val CLUSTER_ID = "cluster_id"
        val INVITE_LINK_ACTIVE = "invite_link_active"

        object REQUEST_CODE {
            const val REQUEST_CLUSTER = 2001
        }
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
        const val EXTRA_INTENT_TYPE = "EXTRA_INTENT_TYPE"
        const val EXTRA_INTENT_TYPE_SHARE = "EXTRA_INTENT_TYPE_SHARE"
        const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"
        const val EXTRA_QUESTION_ITEM = "EXTRA_QUESTION_ITEM"
        const val EXTRA_QUIZ_PARTICIPATION_ID = "EXTRA_QUIZ_PARTICIPATION_ID"
        const val EXTRA_ACHIEVED_ID = "EXTRA_ACHIEVED_ID"
        const val EXTRA_ACHIEVED_BADGE_ITEM = "EXTRA_ACHIEVED_BADGE_ITEM"
        const val EXTRA_SEARCH_KEYWORD = "EXTRA_SEARCH_KEYWORD"
        const val EXTRA_IS_SEARCH_FILTER = "EXTRA_IS_SEARCH_FILTER"
        const val EXTRA_LOTTIE_FILE_NAME = "EXTRA_LOTTIE_FILE_NAME"
        const val EXTRA_LOTTIE_AUTOPLAY = "EXTRA_LOTTIE_AUTOPLAY"
        const val EXTRA_ONBOARDING_TITLE = "EXTRA_ONBOARDING_TITLE"
        const val EXTRA_ONBOARDING_DESC = "EXTRA_ONBOARDING_DESC"
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
        const val RC_OPEN_DETAIL_QUESTION = 1012
        const val RC_FILTER_CLUSTER = 1013
    }

    object ResultCode {
        const val RESULT_DELETE_ITEM_JANPOL = 111
        const val RESULT_DELETE_ITEM_QUESTION = 112
        const val RESULT_ITEM_CHANGED_QUESTION = 113
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

        const val INVITATION_PATH = "invitation?"
        const val ACCEPT_CLUSTER_INVITATION_PATH = "invitation/accept_existing"
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
        const val CATEGORY_ITEM = 8
        const val TYPE_PASLON = 9
        const val TYPE_USER = 10
        const val TYPE_PARTAI = 11
    }

    object Profile {
        const val BASE_URL_PUSAT_BANTUAN = "https://pantaubersama.pusatbantuan.com/"
        const val URL_PUSAT_BANTUAN = BASE_URL_PUSAT_BANTUAN
        const val URL_PANDUAN_KOMUNITAS = BASE_URL_PUSAT_BANTUAN + "2/2"
        const val URL_TENTANG_PANTAU_BERSAMA = BASE_URL_PUSAT_BANTUAN + "3/4"
        const val USER_ID = "user_id"
        const val PROFILE_REQUEST_CODE = 112
    }

    object Filter {
        object Pilpres { // value dari backend
            const val FILTER_ALL = "team_all"
            const val FILTER_TEAM_1 = "team_id_1"
            const val FILTER_TEAM_2 = "team_id_2"
            const val FILTER_TEAM_3 = "team_id_3"
            const val FILTER_TEAM_4 = "team_id_4"
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
        const val EMAIL =
                "^[A-Za-z][A-Za-z0-9]*([._-]?[A-Za-z0-9]+)@[A-Za-z].[A-Za-z]{0,3}?.[A-Za-z]{0,2}\$"
    }

    object Permission {
        val GET_IMAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    object CONNECT {
        const val FACEBOOK = "facebook"
        const val TWITTER = "twitter"
    }

    object Share {
        const val SHARE_FEEDS_PATH = "share/pilpres/"
        const val SHARE_JANPOL_PATH = "share/janjipolitik/"
        const val SHARE_TANYA_PATH = "share/tanya/"
        const val SHARE_KUIS_PATH = "share/kuis/"
        const val SHARE_HASIL_KUIS_PATH = "share/hasilkuis/"
        const val SHARE_KECENDERUNGAN_PATH = "share/kecenderungan/"
        const val SHARE_BADGE_PATH = "share/badge/"
    }

    object Onboarding {
        const val ONBOARDING_1_TITLE = "Voice Over Noise"
        const val ONBOARDING_1_DESC = "Linimasa konten kampanye resmi tanpa sampah Informasi."
        const val ONBOARDING_1_FILE_NAME = "lottie_onboarding_1_linimasa.json"
        const val ONBOARDING_2_TITLE = "Understanding Over Branding"
        const val ONBOARDING_2_DESC = "Fleksibilitas uji preferensi kebijakan kandidat calon pemimpin."
        const val ONBOARDING_2_FILE_NAME = "lottie_onboarding_2_penpol.json"
        const val ONBOARDING_3_TITLE = "Chivalry Over Bigotry"
        const val ONBOARDING_3_DESC = "Ruang adu argumentasi sehat dan berkualitas."
        const val ONBOARDING_3_FILE_NAME = "lottie_onboarding_3_wordstadium.json"
        const val ONBOARDING_4_TITLE = "Inspector Over Spectator"
        const val ONBOARDING_4_DESC = "Kemudahan pelaporan dugaan pelanggaran Pemilu 2019."
        const val ONBOARDING_4_FILE_NAME = "lottie_onboarding_4_lapor.json"
        const val ONBOARDING_5_TITLE = "Festivity Over Apathy"
        const val ONBOARDING_5_DESC = "Rekapitulasi partisipatif real-time dan transparan."
        const val ONBOARDING_5_FILE_NAME = "lottie_onboarding_5_perhitungan.json"
    }
}