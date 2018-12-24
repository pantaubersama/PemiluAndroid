package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.content.Context
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.data.model.tweet.PilpresTweet

/**
 * @author edityomurti on 19/12/2018 14:17
 */
class PilpresAdapter(context: Context) : BaseAdapter<PilpresTweet, PilpresViewHolder>(context) {

    override fun initViewHolder(view: View, viewType: Int): PilpresViewHolder {
        return PilpresViewHolder(view, itemClickListener, itemLongClickListener)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_pilpres_tweet
    }
}