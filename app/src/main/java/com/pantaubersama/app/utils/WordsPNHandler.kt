package com.pantaubersama.app.utils

import com.pantaubersama.app.data.model.debat.WordConstants
import com.pantaubersama.app.data.model.debat.WordItem
import io.reactivex.Completable

/**
 * @author edityomurti on 06/03/2019 14:57
 */
class WordsPNHandler {
    private var onGotNewWordsListener: OnGotNewWordsListener? = null

    fun handleWordsPN(wordItem: WordItem): Completable {
        return if (wordItem.type == WordConstants.Type.COMMENT) {
            Completable.fromCallable { onGotNewWordsListener?.gotNewComment(wordItem) }
        } else {
            Completable.fromCallable { onGotNewWordsListener?.gotNewArgumen(wordItem) }
        }
    }

    fun setOnGotNewWordsListener(onGotNewWordsListener: OnGotNewWordsListener?) {
        this.onGotNewWordsListener = onGotNewWordsListener
    }

    interface OnGotNewWordsListener {
        fun gotNewArgumen(word: WordItem)
        fun gotNewComment(word: WordItem)
    }
}