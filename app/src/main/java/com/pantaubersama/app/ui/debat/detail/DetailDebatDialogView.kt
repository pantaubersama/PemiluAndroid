package com.pantaubersama.app.ui.debat.detail

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink

/**
 * @author edityomurti on 06/03/2019 18:19
 */
interface DetailDebatDialogView : BaseView {
    fun showLoadingUrlPreview()
    fun dismissLoadingUrlPreview()
    fun showTweetPreview(oEmbedLink: OEmbedLink)
    fun showUrlPreview(urlItem: UrlItem)
    fun onErrorUrlPreview(t: Throwable)
}