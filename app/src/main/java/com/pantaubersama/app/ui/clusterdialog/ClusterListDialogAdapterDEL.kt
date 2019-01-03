package com.pantaubersama.app.ui.clusterdialog

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapterDEL
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.cluster.ClusterItem
import kotlinx.android.synthetic.main.item_cluster.*

/**
 * @author edityomurti on 27/12/2018 00:16
 */
@Deprecated("need to implement BaseRecyclerAdapter")
class ClusterListDialogAdapterDEL(context: Context) : BaseAdapterDEL<ClusterItem,
    ClusterListDialogAdapterDEL.ClusterListViewHolder>(context),
    Filterable {

    var listener: ClusterListDialogAdapterDEL.AdapterListener? = null
    private var originalData: MutableList<ClusterItem> = ArrayList()

    override fun add(items: MutableList<ClusterItem>) {
        super.add(items)
        originalData.addAll(items)
    }

    inner class ClusterListViewHolder(
        override val containerView: View?
    ) : BaseViewHolder<ClusterItem>(
        containerView!!) {

        override fun bind(item: ClusterItem) {
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = item.name
            ll_cluster_container.setOnClickListener { listener?.onClick(item) }
        }
    }

    override fun initViewHolder(view: View, viewType: Int): ClusterListViewHolder {
        return ClusterListViewHolder(view)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_cluster
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString()
                var filterResults = FilterResults()
                if (query.isEmpty()) {
                    filterResults.values = originalData
                } else {
                    var filteredList: MutableList<ClusterItem> = ArrayList()
                    for (cluster in originalData) {
                        if (cluster.name?.toLowerCase()?.contains(query.toLowerCase())!!) {
                            filteredList.add(cluster)
                        }
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraints: CharSequence?, results: FilterResults?) {
                data = results?.values as (ArrayList<ClusterItem>)
                notifyDataSetChanged()
            }
        }
    }

    interface AdapterListener {
        fun onClick(item: ClusterItem)
    }
}