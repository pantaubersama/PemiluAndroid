package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.view.View
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.tweet.PilpresTweet
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pilpres_tweet.*

/**
 * @author edityomurti on 19/12/2018 14:18
 */
class PilpresViewHolder(
    override val containerView: View?,
    itemClickListener: OnItemClickListener?,
    itemLongClickListener: OnItemLongClickListener?
) : BaseViewHolder<PilpresTweet>(
    containerView!!,
    itemClickListener,
    itemLongClickListener),
    LayoutContainer {

    override fun bind(item: PilpresTweet) {
        tv_tweet_content.text = item.tweetContent
    }
}