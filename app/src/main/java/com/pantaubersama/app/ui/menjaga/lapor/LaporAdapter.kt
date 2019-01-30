package com.pantaubersama.app.ui.menjaga.lapor

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.LoadingModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.header_item_layout.*

class LaporAdapter : BaseRecyclerAdapter() {
    var listener: LaporAdapter.Listener? = null

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is BannerInfo) {
            VIEW_TYPE_BANNER
        } else if (data[position] is LoadingModel) {
            VIEW_TYPE_LOADING
        } else if (data[position] is Profile) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    inner class LaporViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
    }

    inner class HeaderViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun onBind(item: Profile) {
            header_user_avatar.loadUrl(item.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
            user_name.text = item.name
            header_hint.text = itemView.context.getString(R.string.lapor_short_hint)
            question_section.setOnClickListener {
                listener?.onClickHeader()
            }
        }
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

    fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.header_item_layout))
            VIEW_TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            else -> LaporViewHolder(parent.inflate(R.layout.lapor_item_layout))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? LoadingViewHolder)?.bind()
        (holder as? HeaderViewHolder)?.onBind(data[position] as Profile)
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
//        (holder as? LaporViewHolder)?.onBind(data[position] as Pertanyaan)
    }

    fun addHeader(profile: Profile) {
        if (data.size != 0 && data[0] is BannerInfo) {
            addItem(profile, 1)
        } else {
            addItem(profile, 0)
        }
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    companion object {
        var VIEW_TYPE_LOADING = 0
        var VIEW_TYPE_HEADER = 1
        var VIEW_TYPE_ITEM = 2
        var VIEW_TYPE_BANNER = 3
    }

    interface Listener {
        fun onClickHeader()
        fun onClickBanner(banner: BannerInfo)
    }
}