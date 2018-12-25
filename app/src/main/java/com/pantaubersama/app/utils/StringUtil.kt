package com.pantaubersama.app.utils

import android.util.Patterns

/**
 * Created by alimustofa on 11/01/18.
 */
class StringUtil {
    companion object {
        fun getStringFromArray(someList: MutableList<String>?): String? {
            val sb = StringBuilder()
            for (s in someList!!) {
                sb.append(s)
                sb.append(", ")
            }
            return sb.toString()
        }

        fun pullUrlFromString(someString: String): String {
            var link = ""
//            val regex = "\\(?\\b(http://|https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]"
//            val p = Pattern.compile(regex)
            val pattern = Patterns.WEB_URL
            val m = pattern.matcher(someString)
            while (m.find()) {
                var urlStr: String = m.group()
                if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                    urlStr = urlStr.substring(1, urlStr.length - 1)
                }
                link = urlStr
            }
            return link
        }
    }
}