package com.pantaubersama.app.utils

import android.text.Editable
import android.text.Html

import org.xml.sax.XMLReader

import java.util.Vector

class HtmlTagHandler : Html.TagHandler {
    private var mListItemCount = 0
    private val mListParents = Vector<String>()
    private val mListCounter = Vector<Int>()

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag == "unordered" || tag == "ordered") {
            if (opening) {
                mListParents.add(mListParents.size, tag)
                mListCounter.add(mListCounter.size, 0)
            } else {
                mListParents.removeElementAt(mListParents.size - 1)
                mListCounter.removeElementAt(mListCounter.size - 1)
            }
        } else if (tag.equals("list") && opening) {
            handleListTag(output)
        }
    }
//
//    private fun getLast(text: Editable, kind: Class<*>): Any? {
//        val objs = text.getSpans(0, text.length, kind)
//        if (objs.size == 0) {
//            return null
//        } else {
//            for (i in objs.size downTo 1) {
//                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
//                    return objs[i - 1]
//                }
//            }
//            return null
//        }
//    }

    private fun handleListTag(output: Editable) {
        if (mListParents.lastElement() == "unordered") {
            if (output.isNotEmpty())
                output.append("\n")
            for (i in 1 until mListCounter.size)
                output.append("\t")
            output.append("• ")
        } else if (mListParents.lastElement() == "ordered") {
            mListItemCount = mListCounter.lastElement() + 1
            if (output.isNotEmpty())
                output.append("\n")
            for (i in 1 until mListCounter.size)
                output.append("\t")
            output.append(mListItemCount.toString() + " ")
            mListCounter.removeElementAt(mListCounter.size - 1)
            mListCounter.add(mListCounter.size, mListItemCount)
        }
    }

    companion object {
        fun customizeListTags(html: String?): String? {
            if (html == null) {
                return null
            }
            var newHtml = html
            val UL = "unordered"
            val OL = "ordered"
            val LI = "list"
            newHtml = newHtml.replace("<ul", "<$UL")
            newHtml = newHtml.replace("</ul>", "</$UL>")
            newHtml = newHtml.replace("<ol", "<$OL")
            newHtml = newHtml.replace("</ol>", "</$OL>")
            newHtml = newHtml.replace("<li", "<" + LI)
            newHtml = newHtml.replace("</li>", "</$LI>")
            return newHtml
        }
    }
}
