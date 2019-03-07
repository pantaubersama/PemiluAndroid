package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.WordItem

/**
 * @author edityomurti on 15/02/2019 12:30
 */

interface DebatView : BaseView {
    fun showWordsFighter(wordList: MutableList<WordItem>)
    fun showLoadingWordsFighter()
    fun dismissLoadingWordsFighter()
    fun onErrorGetWordsFighter(t: Throwable)

    fun showKomentar(komentarList: MutableList<Komentar>)
    fun showLoadingKomentar()
    fun dismissLoadingKomentar()

    fun onSuccessPostWordsFighter(wordItem: WordItem)
    fun onFailedPostWordsFighter(wordItem: WordItem)

    fun updateInputTurn()
}