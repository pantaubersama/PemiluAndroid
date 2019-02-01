package com.pantaubersama.app.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.pantaubersama.app.BuildConfig

class CopyUtil {
    companion object {
        fun copyToClipBoard(context: Context, message: String, alert: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, message)
            clipboard.primaryClip = clip
            ToastUtil.show(context, alert)
        }

        fun copyJanpol(context: Context, janpolId: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, BuildConfig.PANTAU_WEB_URL + PantauConstants.Share.SHARE_JANPOL_PATH + janpolId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }

        fun copyFeedsItem(context: Context, feedsId: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, BuildConfig.PANTAU_WEB_URL + PantauConstants.Share.SHARE_FEEDS_PATH + feedsId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }

        fun copyTanyaKandidat(context: Context, questionId: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, BuildConfig.PANTAU_WEB_URL + PantauConstants.Share.SHARE_TANYA_PATH + questionId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }
    }
}