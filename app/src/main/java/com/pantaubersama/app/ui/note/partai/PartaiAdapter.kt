package com.pantaubersama.app.ui.note.partai

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_partai_pilihan.*

class PartaiAdapter : BaseRecyclerAdapter() {
    lateinit var selectedItem: PoliticalParty
    lateinit var listener: Listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PartaiViewHolder(parent.inflate(R.layout.item_partai_pilihan))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartaiViewHolder).bind(data[position] as PoliticalParty)
    }

    inner class PartaiViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(politicalParty: PoliticalParty) {
            partai_pilihan_nama.text = politicalParty.name
            partai_pilihan_no_urut.text = "No Urut ${politicalParty.number}"
            if (politicalParty.image?.medium?.url != null) {
                partai_pilihan_nama_container.gravity = Gravity.CENTER_VERTICAL
                partai_pilihan_foto_partai.visibility = View.VISIBLE
                partai_pilihan_no_urut.visibility = View.VISIBLE
                partai_pilihan_foto_partai.loadUrl(politicalParty.image?.medium?.url, R.drawable.ic_avatar_placeholder)
            } else {
                partai_pilihan_nama_container.gravity = Gravity.CENTER
                partai_pilihan_foto_partai.visibility = View.GONE
                partai_pilihan_no_urut.visibility = View.GONE
            }
            if (selectedItem == politicalParty) {
                partai_pilihan_container.setBackgroundResource(R.drawable.bg_rounded_fill_orange)
                partai_pilihan_nama.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                partai_pilihan_no_urut.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                partai_pilihan_container.setBackgroundResource(R.drawable.bg_rounded_outline_white)
                partai_pilihan_nama.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_dark_3))
                partai_pilihan_no_urut.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_dark_3))
            }
            itemView.setOnClickListener {
                setSelectedData(politicalParty)
            }
        }
    }

    fun setSelectedData(politicalParty: PoliticalParty) {
        selectedItem = politicalParty
        notifyDataSetChanged()
        listener.onSelectItem(selectedItem)
    }

    interface Listener {
        fun onSelectItem(politicalParty: PoliticalParty)
    }
}