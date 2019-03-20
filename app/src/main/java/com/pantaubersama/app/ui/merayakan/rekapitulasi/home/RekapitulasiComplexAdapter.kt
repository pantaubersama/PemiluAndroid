package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData
import com.pantaubersama.app.ui.widget.BannerViewHolder
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rekapitulasi_item.*
import kotlinx.android.synthetic.main.rekapitulasi_nasional_layout.*
import kotlinx.android.synthetic.main.rekapitulasi_total_participants_layout.*

class RekapitulasiComplexAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun getItemViewType(position: Int): Int {
        if (data[position] is BannerInfo) {
            return VIEW_TYPE_BANNER
        } else if (data[position] is TotalParticipantData && position == 1) {
            return VIEW_TYPE_TOTAL_PARTICIPANT
        } else if (data[position] is Percentage && position == 2) {
            return VIEW_TYPE_NASIONAL
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
            VIEW_TYPE_TOTAL_PARTICIPANT -> RekapitulasiTotalParticipantViewHolder(parent.inflate(R.layout.rekapitulasi_total_participants_layout))
            VIEW_TYPE_NASIONAL -> RekapitulasiNasionalViewHolder(parent.inflate(R.layout.rekapitulasi_nasional_layout))
            VIEW_TYPE_FOOTER -> RekapitulasiFooterViewHolder(parent.inflate(R.layout.rekapitulasi_footer_layout))
            else -> RekapitulasiViewHolder(parent.inflate(R.layout.rekapitulasi_item))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? RekapitulasiTotalParticipantViewHolder)?.bind(data[position] as TotalParticipantData)
        (holder as? RekapitulasiNasionalViewHolder)?.bind(data[position] as Percentage)
        (holder as? RekapitulasiViewHolder)?.bind(data[position] as Percentage)
        (holder as? RekapitulasiFooterViewHolder)?.bind()
    }

    inner class RekapitulasiNasionalViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Percentage) {
            // paslon 1
            main_paslon_1_percentage.text = String.format("%.2f", item.candidates[0].percentage)
            val p1 = paslon_1_strength.layoutParams as LinearLayout.LayoutParams
            item.candidates[0].percentage?.toFloat()?.let {
                p1.weight = it
            }
            paslon_1_strength.layoutParams = p1
            // paslon 2
            main_paslon_2_percentage.text = String.format("%.2f", item.candidates[1].percentage)
            val p2 = paslon_2_strength.layoutParams as LinearLayout.LayoutParams
            item.candidates[1].percentage?.toFloat()?.let {
                p2.weight = it
            }
            paslon_2_strength.layoutParams = p2
        }
    }

    inner class RekapitulasiTotalParticipantViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: TotalParticipantData) {
            total_participants.text = item.total.toString()
        }
    }

    inner class RekapitulasiViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Percentage) {
            province_text.text = item.region.name
            // paslon 1
            paslon_1_name.text = "Jokowi - Ma'ruf"
            paslon_1_percentage.text = item.candidates[0].percentage.toString()
            // paslon 2
            paslon_2_name.text = "Prabowo - Sandi"
            paslon_2_percentage.text = item.candidates[1].percentage.toString()
            // golput
            golput_count.text = item.invalidVote.percentage.toString()
            itemView.setOnClickListener {
                listener?.onClickItem(item)
            }
        }
    }

    inner class RekapitulasiFooterViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
        }
    }

    companion object {
        var VIEW_TYPE_ITEM = 1
        var VIEW_TYPE_BANNER = 2
        var VIEW_TYPE_TOTAL_PARTICIPANT = 3
        var VIEW_TYPE_NASIONAL = 4
        var VIEW_TYPE_FOOTER = 5
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

    fun addHeader(data: Percentage) {
        addItem(data, 1)
    }

    interface Listener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickItem(item: Percentage)
    }
}