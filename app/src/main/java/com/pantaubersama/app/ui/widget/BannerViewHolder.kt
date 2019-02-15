package com.pantaubersama.app.ui.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*

class BannerViewHolder(
    override val containerView: View,
    private val onClick: (Int) -> Unit,
    private val onRemove: () -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        rl_banner_container.setOnClickListener { onClick(adapterPosition) }
        iv_banner_close.setOnClickListener { onRemove() }
    }

    fun bind(item: BannerInfo) {
        tv_banner_text.text = item.body
        iv_banner_image.loadUrl(item.headerImage?.url)
    }
}