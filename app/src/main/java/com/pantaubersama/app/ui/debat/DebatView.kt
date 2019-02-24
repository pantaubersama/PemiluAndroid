package com.pantaubersama.app.ui.debat

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.MessageItem

/**
 * @author edityomurti on 15/02/2019 12:30
 */

interface DebatView : BaseView {
    fun showMessage(messageList: MutableList<MessageItem>)

    fun showKomentar(komentarList: MutableList<Komentar>)
    fun showLoadingKomentar()
    fun dismissLoadingKomentar()

    fun onSuccessPostMessage(messageItem: MessageItem)
    fun onFailedPostMessage(messageItem: MessageItem)
}