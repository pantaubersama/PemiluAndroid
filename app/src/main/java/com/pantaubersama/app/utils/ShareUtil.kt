package com.pantaubersama.app.utils

import android.content.Context
import android.content.Intent
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.tweet.PilpresTweet

/**
 * @author edityomurti on 25/12/2018 20:03
 */
class ShareUtil() {
    companion object {
        fun shareItem(context: Context, item: Any?) {
            val targetedShareIntents: MutableList<Intent> = ArrayList()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val resInfo = context.packageManager?.queryIntentActivities(shareIntent, 0)
            val sharedItem: String = "" + when (item) {
                is Pertanyaan -> "share/tk/" + item.id
                is PilpresTweet -> "share/pilpres/" + item.id
                else -> ""
            }
            if (!resInfo!!.isEmpty()) {
                for (resolveInfo in resInfo) {
                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")

                    sendIntent.putExtra(Intent.EXTRA_TEXT, "pantau.co.id/$sharedItem")
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