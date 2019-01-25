package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_BANNER
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_FEEDS
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.item_pilpres_tweet.*

class PilpresAdapter(private val isTwitterAvailable: Boolean) : BaseRecyclerAdapter() {

    var listener: PilpresAdapter.AdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            TYPE_FEEDS -> FeedViewHolder(parent.inflate(R.layout.item_pilpres_tweet))
            else -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? FeedViewHolder)?.bind(data[position] as FeedsItem)
        (holder as? LoadingViewHolder)?.bind()
    }

    inner class BannerViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: BannerInfo) {
            tv_banner_text.text = item.body
            iv_banner_image.loadUrl(item.headerImage?.url)
            rl_banner_container.setOnClickListener { listener?.onClickBanner(item) }
            iv_banner_close.setOnClickListener { removeBanner() }
        }
    }

    inner class FeedViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bind(item: FeedsItem) {
            iv_tweet_avatar.loadUrl(item.account?.profileImageUrl, R.drawable.ic_avatar_placeholder)
            tv_tweet_name.text = item.account?.name
            tv_tweet_username.text = "@" + item.account?.username
            tv_tweet_content.text = item.source?.text
            iv_team_avatar.loadUrl(item.team?.avatar, R.drawable.ic_avatar_placeholder)
            tv_team_name.text = itemView.context.getString(R.string.txt_disematkan_dari) + " " + item.team?.title
            rl_item_pilpres_tweet.setOnClickListener { listener?.onClickTweetContent(item) }

            iv_tweet_option.visibleIf(isTwitterAvailable)
            iv_tweet_option.setOnClickListener { listener?.onClickTweetOption(item) }
            tv_tweet_date.text = item.createdAtInWord?.id
        }
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    interface AdapterListener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickTweetContent(item: FeedsItem)
        fun onClickTweetOption(item: FeedsItem)
        fun onClickShare(item: FeedsItem)
    }
}