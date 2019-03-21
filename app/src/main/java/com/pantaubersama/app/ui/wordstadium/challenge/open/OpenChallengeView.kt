package com.pantaubersama.app.ui.wordstadium.challenge.open

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink

interface OpenChallengeView : BaseView {
    fun showProfile(profile: Profile)
    fun onSuccessTweetPreview(oEmbedLink: OEmbedLink)

    fun showLoadingUrlPreview()
    fun dismissLoadingUrlPreview()
    fun showUrlPreview(urlItem: UrlItem)
    fun onErrorUrlPreview(t: Throwable)
}