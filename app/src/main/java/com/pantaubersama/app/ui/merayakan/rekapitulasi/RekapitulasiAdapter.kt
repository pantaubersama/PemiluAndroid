package com.pantaubersama.app.ui.merayakan.rekapitulasi

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rekapitulasi_item.*

class RekapitulasiAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RekapitulasiViewHolder(parent.inflate(R.layout.rekapitulasi_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RekapitulasiViewHolder).bind(data[position] as RekapitulasiData)
    }

    inner class RekapitulasiViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: RekapitulasiData) {
            province_text.text = item.location
            // paslon 1
            paslon_1_name.text = item.teams[0].team.title
            paslon_1_percentage.text = "${item.teams[0].percentage}%"
            // paslon 2
            paslon_2_name.text = item.teams[1].team.title
            paslon_2_percentage.text = "${item.teams[1].percentage}%"
            // golput
            golput_count.text = (10).toString() // example
            votes_count.text = item.totalParticipant.toInt().toString()
            itemView.setOnClickListener {
                listener?.onClickItem(item)
            }
        }
    }

    interface Listener {
        fun onClickItem(item: RekapitulasiData)
    }
}