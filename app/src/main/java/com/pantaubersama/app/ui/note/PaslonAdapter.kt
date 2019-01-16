package com.pantaubersama.app.ui.note

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.capres.PaslonData
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pilihan.*


class PaslonAdapter : BaseRecyclerAdapter() {
    var selectedItem: PaslonData? = null
    lateinit var listener: Listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CapressViewHolder(parent.inflate(R.layout.item_pilihan))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CapressViewHolder).bind(data[position] as PaslonData)
    }

    inner class CapressViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(paslonData: PaslonData) {
            if (paslonData.paslonImage != null) {
                paslonData.paslonImage?.let { pilihan_foto_calon.setImageResource(it) }
                pilihan_nama_calon.text = paslonData.paslonName
            } else {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                paslon_name_container.setPadding(16f.toDp(), 12f.toDp(), 16f.toDp(), 12f.toDp())
                pilihan_nama_calon.text = paslonData.paslonName
            }
            if (paslonData == selectedItem) {
                paslon_container.setBackgroundResource(R.drawable.bg_rounded_fill_orange)
                pilihan_nama_calon.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                paslon_container.setBackgroundResource(R.drawable.bg_rounded_outline_white)
                pilihan_nama_calon.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_5))
            }
            itemView.setOnClickListener {
                setSelectedItem(paslonData)
            }
        }

        private fun setSelectedItem(paslonData: PaslonData) {
            selectedItem = paslonData
            selectedItem?.let { listener.onSelectItem(it) }
            notifyDataSetChanged()
        }

        private fun Float.toDp(): Int {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, itemView.resources.displayMetrics))
        }

    }

    interface Listener {
        fun onSelectItem(paslonData: PaslonData)
    }
}