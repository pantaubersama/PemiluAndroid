package com.pantaubersama.app.utils

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.AbsoluteSizeSpan
import android.text.style.UnderlineSpan

fun spannable(init: SpanWithChildren.() -> Unit): SpanWithChildren {
    val spanWithChildren = SpanWithChildren()
    spanWithChildren.init()
    return spanWithChildren
}

abstract class Span {
    abstract fun render(builder: SpannableStringBuilder)

    fun toCharSequence(): CharSequence {
        val builder = SpannableStringBuilder()
        render(builder)
        return builder
    }
}

class SpanWithChildren(val what: Any? = null) : Span() {
    val children = ArrayList<Span>()

    fun textColor(color: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren = span(ForegroundColorSpan(color), init)

    fun typeface(typeface: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren =
            span(StyleSpan(typeface), init)

    fun textSize(size: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren =
            span(AbsoluteSizeSpan(size, true), init)

    fun bold(init: SpanWithChildren.() -> Unit): SpanWithChildren = typeface(Typeface.BOLD, init)

    fun underline(init: SpanWithChildren.() -> Unit): SpanWithChildren = span(UnderlineSpan(), init)

    fun span(what: Any, init: SpanWithChildren.() -> Unit): SpanWithChildren {
        val child = SpanWithChildren(what)
        child.init()
        children.add(child)
        return this
    }

    operator fun String.unaryPlus() {
        children.add(SpanWithText(this))
    }

    override fun render(builder: SpannableStringBuilder) {
        val start = builder.length

        for (c in children) {
            c.render(builder)
        }

        if (what != null) {
            builder.setSpan(what, start, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }
}

class SpanWithText(val content: Any) : Span() {
    override fun render(builder: SpannableStringBuilder) {
        builder.append(content.toString())
    }
}