package com.pantaubersama.app.ui.linimasa.janjipolitik.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.LoadingModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.item_janji_politik.*
import kotlinx.android.synthetic.main.layout_action_post.*

class JanjiPolitikAdapter : BaseRecyclerAdapter<JanjiPolitik, RecyclerView.ViewHolder>() {

    var listener: JanjiPolitikAdapter.AdapterListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is BannerInfo -> TYPE_BANNER
            is LoadingModel -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            TYPE_ITEM -> JanjiPolitikViewHolder((parent.inflate(R.layout.item_janji_politik)))
            else -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? JanjiPolitikViewHolder)?.bind(data[position] as JanjiPolitik)
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

    inner class JanjiPolitikViewHolder(override val containerView: View?)
        : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind(item: JanjiPolitik) {
            iv_user_avatar.loadUrl(item.creator?.avatar?.url, R.drawable.ic_avatar_placeholder)
            tv_user_name.text = item.creator?.fullName
            tv_user_cluster.text = item.creator?.cluster?.name
            tv_janpol_title.text = item.title
            tv_janpol_content.text = item.body

            ll_janpol_content.setOnClickListener { listener?.onClickJanPolContent(item) }
            iv_share_button.setOnClickListener { listener?.onClickShare(item) }
            iv_options_button.setOnClickListener { listener?.onClickJanpolOption(item, adapterPosition) }
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

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    interface AdapterListener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickJanPolContent(item: JanjiPolitik)
        fun onClickJanpolOption(item: JanjiPolitik, position: Int)
        fun onClickShare(item: JanjiPolitik)
        fun onClickCopyUrl(id: String?)
        fun onClickLapor(id: String?)
    }
}