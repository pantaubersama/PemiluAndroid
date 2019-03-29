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
import com.pantaubersama.app.data.model.rekapitulasi.Rekapitulasi
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData
import com.pantaubersama.app.ui.widget.BannerViewHolder
import com.pantaubersama.app.utils.* // ktlint-disable
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rekapitulasi_footer_layout.*
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
        } else if (data[position] is Rekapitulasi && position > 2) {
            return VIEW_TYPE_ITEM
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
        (holder as? RekapitulasiViewHolder)?.bind(data[position] as Rekapitulasi)
        (holder as? RekapitulasiFooterViewHolder)?.bind()
    }

    inner class RekapitulasiNasionalViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Percentage) {
            // paslon 1
            main_paslon_1_percentage.text = String.format("%.2f", item.candidates?.get(0)?.percentage)
            paslon_1_average.text = "Rata-rata ${item.candidates?.get(0)?.totalVote} suara"
            val p1 = paslon_1_strength.layoutParams as LinearLayout.LayoutParams
            item.candidates?.get(0)?.percentage?.toFloat()?.let {
                p1.weight = it
            }
            paslon_1_strength.layoutParams = p1
            // paslon 2
            main_paslon_2_percentage.text = String.format("%.2f", item.candidates?.get(1)?.percentage)
            paslon_2_average.text = "Rata-rata ${item.candidates?.get(1)?.totalVote} suara"
            val p2 = paslon_2_strength.layoutParams as LinearLayout.LayoutParams
            item.candidates?.get(1)?.percentage?.toFloat()?.let {
                p2.weight = it
            }
            paslon_2_strength.layoutParams = p2
        }
    }

    inner class RekapitulasiTotalParticipantViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: TotalParticipantData) {
            total_participants.text = item.total.toString()
            last_update_rekapitulasi.text = "Pembaruan terakhir ${item.lastUpdate.createdAtInWord.id}"
        }
    }

    inner class RekapitulasiViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Rekapitulasi) {
            province_text.text = item.region.name
            paslon_1_name.text = "Jokowi - Ma'ruf"
            paslon_2_name.text = "Prabowo - Sandi"
            if (item.percentages != null) {
                item.percentages?.candidates?.get(0)?.percentage?.let {
                    paslon_1_percentage.text = "${String.format("%.2f", it)}%"
                }
                item.percentages?.candidates?.get(1)?.percentage?.let {
                    paslon_2_percentage.text = "${String.format("%.2f", it)}%"
                }
                item.percentages?.invalidVote.let {
                    golput_count.text = it?.total.toString()
                }
                item.percentages?.totalVote?.let {
                    votes_count.text = it.toString()
                }
                itemView.setOnClickListener {
                    listener?.onClickItem(item)
                }
            } else {
                paslon_1_percentage.text = "0%"
                paslon_2_percentage.text = "0%"
                golput_count.text = "0"
                votes_count.text = "0"
            }
        }
    }

    inner class RekapitulasiFooterViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
            LinkifySpannedString(itemView.context.getString(R.string.rekapitulasi_footer_hint), footer_text, object : CustomClickableSpan.Callback {
                override fun onClickUrl(url: String?) {
                    ChromeTabUtil(itemView.context).loadUrl(url)
                }
            })
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
        this.data.add(itemCount, Footer())
        notifyItemInserted(itemCount - 1)
    }

    class Footer : ItemModel {
        override fun getType(): Int {
            return 0
        }
    }

    fun addRekapitulasiHeader(data: Percentage) {
        this.data.add(2, data)
        notifyItemInserted(itemCount - 1)
    }

    fun clearData() {
        data.clear()
        clear()
    }

    interface Listener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickItem(item: Rekapitulasi)
    }
}