package com.pantaubersama.app.utils

import android.widget.TextView
import android.text.SpannableString
import android.util.Patterns
import android.text.method.LinkMovementMethod
import android.text.Spanned

class LinkifySpannedString(a: String?, textView: TextView?, callback: CustomClickableSpan.Callback?) {

    init {
        val urlPattern = Patterns.WEB_URL
//        val mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
//        val mention = mentionPattern.matcher(a)
        val weblink = urlPattern.matcher(a)

        val spannableString = SpannableString(a)
        // weblink
        while (weblink.find()) {
            spannableString.setSpan(
                    CustomClickableSpan(textView?.context, weblink.group(), 0, callback), weblink.start(), weblink.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // @mention
//        while (mention.find()) {
//            spannableString.setSpan(
//                    CustomClickableSpan(textView?.context, mention.group(), 1, callback), mention.start(), mention.end(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        }

        textView?.text = spannableString
        textView?.movementMethod = LinkMovementMethod.getInstance()
    }
}