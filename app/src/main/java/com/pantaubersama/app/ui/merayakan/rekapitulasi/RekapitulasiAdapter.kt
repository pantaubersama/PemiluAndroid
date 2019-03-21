package com.pantaubersama.app.ui.merayakan.rekapitulasi

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.rekapitulasi.Rekapitulasi
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rekapitulasi_item.*

class RekapitulasiAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RekapitulasiViewHolder(parent.inflate(R.layout.rekapitulasi_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RekapitulasiViewHolder).bind(data[position] as Rekapitulasi)
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

    interface Listener {
        fun onClickItem(item: Rekapitulasi)
    }
}