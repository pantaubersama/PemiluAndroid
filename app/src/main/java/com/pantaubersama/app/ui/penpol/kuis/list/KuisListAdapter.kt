package com.pantaubersama.app.ui.penpol.kuis.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.kuis.KuisListItem
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.setBackgroundTint
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_kuis.*
import kotlinx.android.synthetic.main.item_kuis_result.*

class KuisListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<KuisListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: KuisListAdapter.AdapterListener? = null

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is KuisListItem.Result) TYPE_RESULT else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_RESULT) ResultViewHolder(parent.inflate(R.layout.item_kuis_result))
        else ItemViewHolder(parent.inflate(R.layout.item_kuis))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemViewHolder)?.bind(data[position] as KuisListItem.Item)
        (holder as? ResultViewHolder)?.bind(data[position] as KuisListItem.Result)
    }

    inner class ItemViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: KuisListItem.Item) {
            val (buttonText, buttonColor) = when (item.state) {
                KuisState.NOT_TAKEN -> "IKUTI" to R.color.orange
                KuisState.COMPLETED -> "HASIL" to R.color.colorAccent
                KuisState.INCOMPLETE -> "LANJUT" to R.color.red_2
            }
            tv_kuis_title.text = "Kuis Minggu ke-${item.week}"
            tv_kuis_count.text = "${item.count} Pertanyaan"
            btn_kuis_open.text = "$buttonText >>"
            btn_kuis_open.setBackgroundTint(buttonColor)
            btn_kuis_open.setOnClickListener {
                when {
                    item.state == KuisState.NOT_TAKEN -> listener?.onClickIkuti(item)
                    item.state == KuisState.INCOMPLETE -> listener?.onClickLanjut(item)
                    item.state == KuisState.COMPLETED -> listener?.onClickHasil(item)
                }
            }
            btn_share.setOnClickListener {
                listener?.onClickShare(item)
            }
        }
    }

    class ResultViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(result: KuisListItem.Result) {
            tv_kuis_result.text = "${result.percentage}% (${result.candidate})"
        }
    }

    companion object {
        private const val TYPE_RESULT = 0
        private const val TYPE_ITEM = 1
    }

    interface AdapterListener {
        fun onClickIkuti(item: KuisListItem.Item)
        fun onClickLanjut(item: KuisListItem.Item)
        fun onClickHasil(item: KuisListItem.Item)
        fun onClickShare(item: KuisListItem.Item)
    }
}