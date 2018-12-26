package com.pantaubersama.app.ui.clusterdialog

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.cluster.ClusterItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cluster.*

/**
 * @author edityomurti on 27/12/2018 00:16
 */
class ClusterListDialogAdapter(context: Context) : BaseAdapter<ClusterItem,
    ClusterListDialogAdapter.ClusterListViewHolder>(context),
    Filterable {

    private var originalData: MutableList<ClusterItem> = ArrayList()

    override fun add(items: List<ClusterItem>) {
        super.add(items)
        originalData.addAll(items)
    }

    inner class ClusterListViewHolder(
        override val containerView: View?,
        itemClickListener: OnItemClickListener?,
        itemLongClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<ClusterItem>(
        containerView!!,
        itemClickListener,
        itemLongClickListener),
        LayoutContainer {

        override fun bind(item: ClusterItem) {
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = item.name
        }

    }

    override fun initViewHolder(view: View, viewType: Int): ClusterListViewHolder {
        return ClusterListViewHolder(view, itemClickListener, itemLongClickListener)
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

}