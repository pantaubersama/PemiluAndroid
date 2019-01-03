package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.linimasa.Feeds
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.item_pilpres_tweet.*
import timber.log.Timber

class PilpresAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {

    var data: MutableList<ItemModel> = ArrayList()
    var listener: PilpresAdapter.AdapterListener? = null

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is BannerInfo -> TYPE_BANNER
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            else -> FeedViewHolder(parent.inflate(R.layout.item_pilpres_tweet))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? FeedViewHolder)?.bind(data[position] as FeedsItem)
    }

    inner class BannerViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: BannerInfo) {
            Timber.e("BannerViewHolder body = ${item.body}")
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
            tv_tweet_username.text = item.account?.username
            tv_tweet_content.text = "@" + item.source?.text
            iv_team_avatar.loadUrl(item.team?.avatar, R.drawable.ic_avatar_placeholder)
            tv_team_name.text = itemView.context.getString(R.string.txt_disematkan_dari) + " " + item.team?.title
            rl_item_pilpres_tweet.setOnClickListener {
                listener?.onClickTweetContent(item)
            }
            iv_tweet_option.setOnClickListener {  }
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

    fun <T : MutableList<ItemModel>> setDatas(items: T) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun <T : MutableList<ItemModel>> addData(items: T) {
        data.addAll(items)
        notifyItemRangeInserted(itemCount, items.size)
    }

    fun <T : ItemModel> addItem(item: T) {
        data.add(item)
        notifyItemInserted(itemCount)
    }

    fun <T : ItemModel> addItem(item: T, position: Int) {
        data.add(position, item)
        notifyItemInserted(position)
    }

    fun deleteItem(position: Int) {
        if (position < itemCount) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    interface AdapterListener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickTweetContent(item: FeedsItem)
        fun onClickTweetOption(item: FeedsItem)
        fun onClickShare(item: FeedsItem)
    }
}