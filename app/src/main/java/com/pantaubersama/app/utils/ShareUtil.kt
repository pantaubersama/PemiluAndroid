package com.pantaubersama.app.utils

import android.content.Context
import android.content.Intent
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_FEEDS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_JANPOL_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KECENDERUNGAN_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KUIS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_TANYA_PATH

/**
 * @author edityomurti on 25/12/2018 20:03
 */
class ShareUtil {
    companion object {
        fun shareItem(context: Context, item: Any?) {
            val targetedShareIntents: MutableList<Intent> = ArrayList()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val resInfo = context.packageManager?.queryIntentActivities(shareIntent, 0)
            val sharedItem: String = "" + when (item) {
                is FeedsItem -> "\"${item.source?.text} – " + BuildConfig.PANTAU_BASE_URL + SHARE_FEEDS_PATH + item.id // not used
                is Pertanyaan -> "Menurutmu gimana? " + BuildConfig.PANTAU_BASE_URL + SHARE_TANYA_PATH + item.id
                is JanjiPolitik -> "Sudah tahu Janji yang ini, belum? Siap-siap catatan, ya! ✔ " + BuildConfig.PANTAU_BASE_URL + SHARE_JANPOL_PATH + item.id
                is KuisItem -> "\"${item.title}\" – " + BuildConfig.PANTAU_BASE_URL + SHARE_KUIS_PATH + item.id
                is KuisUserResult -> "Hmm.. Ternyataa \uD83D\uDC40 %s".format(BuildConfig.PANTAU_BASE_URL + SHARE_KECENDERUNGAN_PATH + item.user.id)
                else -> ""
            }
            if (!resInfo!!.isEmpty()) {
                for (resolveInfo in resInfo) {
                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sharedItem)
                    sendIntent.type = "text/plain"
                    if (!resolveInfo.activityInfo.packageName.contains("pantaubersama")) {
                        sendIntent.`package` = resolveInfo.activityInfo.packageName
                        targetedShareIntents.add(sendIntent)
                    }
                }
                val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Bagikan dengan")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())
                context.startActivity(chooserIntent)
            }
        }
    }
}