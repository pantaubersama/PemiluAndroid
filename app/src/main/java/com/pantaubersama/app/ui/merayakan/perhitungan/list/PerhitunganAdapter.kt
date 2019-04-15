package com.pantaubersama.app.ui.merayakan.perhitungan.list

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.ui.widget.BannerViewHolder
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tps_info_item.*

class PerhitunganAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position] is BannerInfo -> VIEW_TYPE_BANNER
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container),
                onClick = { listener?.onClickBanner(data[it] as BannerInfo) },
                onRemove = { removeBanner() })
            else -> PerhitunganViewHolder(parent.inflate(R.layout.tps_info_item))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? PerhitunganViewHolder)?.bind(data[position] as TPS)
    }

    inner class PerhitunganViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: TPS) {
            tps_number.text = "TPS Nomor ${item.tps}"
            province_text.text = item.province.name
            regency_text.text = item.regency.name
            district_text.text = item.district.name
            village_text.text = item.village?.name ?: " - "
            when (item.status) {
                "sandbox" -> {
                    quick_count_status.text = "Uji Coba"
                    quick_count_status_container.setBackgroundResource(R.drawable.rounded_gray_dark_2)
                    tps_number.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_3))
                    tps_container.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray_1))
                }
                "local" -> {
                    quick_count_status.text = "Belum Dikirim"
                    quick_count_status_container.setBackgroundResource(R.drawable.rounded_red)
                    tps_number.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                    tps_container.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
                "published" -> {
                    quick_count_status.text = "Terkirim"
                    quick_count_status_container.setBackgroundResource(R.drawable.rounded_green)
                    tps_number.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                    tps_container.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
            }
            tps_options.setOnClickListener {
                listener?.onClickTpsOptions(item, adapterPosition)
            }
            itemView.setOnClickListener {
                listener?.onClickItem(item, adapterPosition)
            }
        }
    }

    companion object {
        var VIEW_TYPE_ITEM = 1
        var VIEW_TYPE_BANNER = 2
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    private fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    interface Listener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickTpsOptions(tps: TPS, position: Int)
        fun onClickItem(item: TPS, adapterPosition: Int)
    }
}