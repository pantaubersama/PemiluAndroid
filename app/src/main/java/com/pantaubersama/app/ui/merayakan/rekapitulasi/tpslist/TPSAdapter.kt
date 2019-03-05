package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tps_user_info_item.*

class TPSAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TPSViewHolder(parent.inflate(R.layout.tps_user_info_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TPSViewHolder).bind(data[position] as TPSData)
    }

    inner class TPSViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: TPSData) {
//            user_avatar.loadUrl(item.user.avatar?.url, R.drawable.ic_avatar_placeholder)
//            user_name.text = item.user.fullName
//            user_cluster.text = item.user.cluster?.name
            tps_number.text = item.tps.toString()
            tps_address.text = "${item.province.name}, ${item.regency.name}, ${item.district.name}, ${item.village.name}"
            itemView.setOnClickListener {
                listener?.onClickItem(item)
            }
        }
    }

    interface Listener {
        fun onClickItem(item: TPSData)
    }
}