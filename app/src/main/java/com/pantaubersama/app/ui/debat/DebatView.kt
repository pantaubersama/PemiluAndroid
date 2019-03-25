package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.WordItem

/**
 * @author edityomurti on 15/02/2019 12:30
 */

interface DebatView : BaseView {
    fun showWordsFighter(wordList: MutableList<WordItem>)
    fun showMoreWordsFighter(wordList: MutableList<WordItem>)
    fun showLoadingWordsFighter()
    fun dismissLoadingWordsFighter()
    fun onEmptyWordsFighter()
    fun onErrorGetWordsFighter(t: Throwable)
    fun onErrorGetMoreWordsFighter(t: Throwable)

    fun showKomentar(komentarList: MutableList<WordItem>)
    fun showLoadingKomentar()
    fun dismissLoadingKomentar()
    fun onEmptyWordsAudience()
    fun onErrorGetWordsAudience(t: Throwable)

    fun onSuccessPostWordsFighter(wordItem: WordItem)
    fun onFailedPostWordsFighter(t: Throwable)

    fun onSuccessPostWordsAudience(wordItem: WordItem)
    fun onFailedPostWordsAudience(t: Throwable)

    fun onSuccessWordsClap()
    fun onErrorWordsClap(t: Throwable)

    fun updateInputTurn()
    fun updateMyTimeLeft()
}