package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import com.pantaubersama.app.ui.widget.BannerViewHolder
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rekapitulasi_item.*
import kotlinx.android.synthetic.main.rekapitulasi_header_layout.*

class RekapitulasiAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun getItemViewType(position: Int): Int {
        if (data[position] is BannerInfo) {
            return VIEW_TYPE_BANNER
        } else if (data[position] is RekapitulasiData && position == 1) {
            return VIEW_TYPE_HEADER
        } else if (data[position] is Footer) {
            return VIEW_TYPE_FOOTER
        } else {
            return VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container),
                onClick = { listener?.onClickBanner(data[it] as BannerInfo) },
                onRemove = { removeBanner() })
            VIEW_TYPE_HEADER -> RekapitulasiHeaderViewHolder(parent.inflate(R.layout.rekapitulasi_header_layout))
            VIEW_TYPE_FOOTER -> RekapitulasiFooterViewHolder(parent.inflate(R.layout.rekapitulasi_footer_layout))
            else -> RekapitulasiViewHolder(parent.inflate(R.layout.rekapitulasi_item))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? RekapitulasiHeaderViewHolder)?.bind(data[position] as RekapitulasiData)
        (holder as? RekapitulasiViewHolder)?.bind(data[position] as RekapitulasiData)
        (holder as? RekapitulasiFooterViewHolder)?.bind()
    }

    inner class RekapitulasiHeaderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: RekapitulasiData) {
            total_votes_count.text = item.totalParticipant.toString()
            // paslon 1
            main_paslon_1_percentage.text = item.teams[0].percentage.toString()
            val p1 = paslon_1_strength.layoutParams as LinearLayout.LayoutParams
            p1.weight = item.teams[0].percentage
            paslon_1_strength.layoutParams = p1
            // paslon 2
            main_paslon_2_percentage.text = item.teams[1].percentage.toString()
            val p2 = paslon_2_strength.layoutParams as LinearLayout.LayoutParams
            p2.weight = item.teams[1].percentage
            paslon_2_strength.layoutParams = p2
        }
    }

    inner class RekapitulasiViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: RekapitulasiData) {
            province_text.text = item.location
            // paslon 1
            paslon_1_name.text = item.teams[0].team.title
            paslon_1_percentage.text = item.teams[0].percentage.toString()
            // paslon 2
            paslon_2_name.text = item.teams[1].team.title
            paslon_2_percentage.text = item.teams[1].percentage.toString()
            // golput
            golput_count.text = item.teams[2].percentage.toString()
        }
    }

    inner class RekapitulasiFooterViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
        }
    }

    companion object {
        var VIEW_TYPE_ITEM = 1
        var VIEW_TYPE_BANNER = 2
        var VIEW_TYPE_HEADER = 3
        var VIEW_TYPE_FOOTER = 4
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    private fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    fun addFooter() {
        addItem(Footer(), data.size)
    }

    class Footer : ItemModel {
        override fun getType(): Int {
            return 0
        }
    }

    fun addHeader(data: RekapitulasiData) {
        addItem(data, 1)
    }

    interface Listener {
        fun onClickBanner(bannerInfo: BannerInfo)
    }
}