package com.pantaubersama.app.utils

import android.content.Context
import android.content.Intent
import android.content.pm.LabeledIntent
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisResult
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.data.model.user.AchievedBadge
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_SHARE
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_BADGE_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_FEEDS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_HASIL_KUIS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_JANPOL_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KECENDERUNGAN_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KUIS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_TANYA_PATH
import com.pantaubersama.app.utils.download.DownloadActivity
import java.io.File

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
                is FeedsItem -> "\"${item.source?.text} #PantauBersama  – " + BuildConfig.PANTAU_WEB_URL + SHARE_FEEDS_PATH + item.id // not used
                is Pertanyaan -> "Kamu setuju pertanyaan ini? Upvote dulu, dong ⬆️ #PantauBersama " + BuildConfig.PANTAU_WEB_URL + SHARE_TANYA_PATH + item.id
                is JanjiPolitik -> "Sudah tahu Janji yang ini, belum? Siap-siap catatan, ya! ✔️ #PantauBersama " + BuildConfig.PANTAU_WEB_URL + SHARE_JANPOL_PATH + item.id
                is KuisItem -> "Iseng-iseng serius main Quiz ini dulu. Kira-kira masih cocok apa ternyata malah nggak cocok, yaa \uD83D\uDE36 #PantauBersama " + BuildConfig.PANTAU_WEB_URL + SHARE_KUIS_PATH + item.id
                is KuisUserResult -> "Hmm.. Ternyata begini kecenderunganku \uD83D\uDC40 #PantauBersama %s".format(BuildConfig.PANTAU_WEB_URL + SHARE_KECENDERUNGAN_PATH + item.user.id)
                is KuisUserResult -> context.getString(R.string.share_wording_kecenderungan).format(BuildConfig.PANTAU_WEB_URL + SHARE_KECENDERUNGAN_PATH + item.user.id)
                is KuisResult -> "Kamu sudah ikut? Aku sudah dapat hasilnya \uD83D\uDE0E #PantauBersama %s".format(BuildConfig.PANTAU_WEB_URL + SHARE_HASIL_KUIS_PATH + item.quizParticipation.id)
                is AchievedBadge -> "Yeay! I got the badge \uD83E\uDD18 #PantauBersama " + BuildConfig.PANTAU_WEB_URL + SHARE_BADGE_PATH + item.achievedId
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

        fun shareApp(context: Context, shareUrl: String) {
            val targetedShareIntents: MutableList<Intent> = ArrayList()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val resInfo = context.packageManager?.queryIntentActivities(shareIntent, 0)

            if (!resInfo!!.isEmpty()) {
                for (resolveInfo in resInfo) {
                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
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

        fun shareImage(context: Context, message: String, imageFile: File) {
//            val imageUri = Uri.parse(imageFile.absolutePath)
            val imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile)
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val targetedShareIntents: MutableList<Intent> = ArrayList()
            val resInfo = context.packageManager?.queryIntentActivities(shareIntent, 0)

            if (!resInfo!!.isEmpty()) {
                for (resolveInfo in resInfo) {
                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.type = "image/*"
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message)
                    sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    if (!resolveInfo.activityInfo.packageName.contains("pantaubersama")) {
                        sendIntent.`package` = resolveInfo.activityInfo.packageName
                        targetedShareIntents.add(sendIntent)
                    }
                }
                val downloadIntent = DownloadActivity.setIntent(context, imageFile.absolutePath)
                targetedShareIntents.add(1, LabeledIntent(downloadIntent, context.packageName, "Simpan", R.drawable.ic_download))
                val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Bagikan ke ..")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())
//                context.startActivity(Intent.createChooser(chooserIntent, "Bagikan ke .."))
                (context as FragmentActivity).startActivityForResult(chooserIntent, RC_SHARE)
            } else {
                context.startActivity(Intent.createChooser(shareIntent, "Bagikan ke .."))
            }
        }
    }
}