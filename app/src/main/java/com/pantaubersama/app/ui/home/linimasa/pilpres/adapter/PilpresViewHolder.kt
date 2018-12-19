package com.pantaubersama.app.ui.home.linimasa.pilpres.adapter

import android.view.View
import android.widget.TextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.tweet.PilpresTweet

/**
 * @author edityomurti on 19/12/2018 14:18
 */
class PilpresViewHolder : BaseViewHolder<PilpresTweet> {

    private var tvTweetContent: TextView

    constructor(itemView: View, itemClickListener: OnItemClickListener?, itemLongClickListener: OnItemLongClickListener?): super(itemView, itemClickListener, itemLongClickListener) {
        tvTweetContent = itemView.findViewById(R.id.tv_tweet_content)
    }

    override fun bind(item: PilpresTweet) {
        tvTweetContent.text = item.tweetContent
    }
}