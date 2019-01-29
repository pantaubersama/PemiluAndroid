package com.pantaubersama.app.ui.menjaga.filter.partiesdialog

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_default_dropdown_filter.*
import kotlinx.android.synthetic.main.item_partai_pilihan.*

class PartiesDialogAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

//    companion object {
//        const val TYPE_DEFAULT = 0
//    }

//    override fun getItemViewType(position: Int): Int {
//        return if (position == 0) {
//            TYPE_DEFAULT
//        } else {
//            PantauConstants.ItemModel.TYPE_CLUSTER_ITEM
//        }
//    }

    inner class PartiesDialogViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: PoliticalParty) {
            partai_pilihan_nama.text = item.name
            if (adapterPosition != 0) {
                partai_pilihan_no_urut.text = "Nomor urut ${item.number}"
                partai_pilihan_foto_partai.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
            } else {
                partai_pilihan_no_urut.visibility = View.GONE
                partai_pilihan_foto_partai.visibility = View.GONE
            }
//            tv_cluster_member_count.text = "${item.memberCount?.let { it } ?: "belum ada"} anggota"

            partai_pilihan_container.setOnClickListener { listener?.onClickItem(item) }
        }
    }

//    inner class DefaultViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
//        fun bind() {
//            tv_cluster_or_party_placeholder.text = itemView.context.getString(R.string.txt_semua_partai)
//            ll_default_filter.setOnClickListener { listener?.onClickDefault() }
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PartiesDialogViewHolder(parent.inflate(R.layout.item_partai_pilihan))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PartiesDialogViewHolder)?.bind(data[position] as PoliticalParty)
//        (holder as? DefaultViewHolder)?.bind()
    }

    interface Listener {
        fun onClickDefault()
        fun onClickItem(party: PoliticalParty)
    }
}