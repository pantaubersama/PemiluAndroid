package com.pantaubersama.app.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_CHALLENGE_PATH

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
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, context.getString(R.string.share_url) + PantauConstants.Share.SHARE_JANPOL_PATH + janpolId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }

        fun copyFeedsItem(context: Context, feedsId: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, context.getString(R.string.share_url) + PantauConstants.Share.SHARE_FEEDS_PATH + feedsId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }

        fun copyTanyaKandidat(context: Context, questionId: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, context.getString(R.string.share_url) + PantauConstants.Share.SHARE_TANYA_PATH + questionId)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }

        fun copyChallenge(context: Context, challenge: Challenge) {
            val baseShareUrl = context.getString(R.string.share_url)
            val shareUrl = "$baseShareUrl$SHARE_CHALLENGE_PATH?challenge_id=${challenge.id}"
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, shareUrl)
            clipboard.primaryClip = clip
            ToastUtil.show(context, "Tautan telah tersalin")
        }
    }
}