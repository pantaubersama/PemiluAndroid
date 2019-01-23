package com.pantaubersama.app.ui.search.cluster

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_CLUSTER_ITEM
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cluster.*

class ClusterAdapter(private var listener: AdapterListener) : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClusterViewHolder(parent.inflate(R.layout.item_cluster))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ClusterViewHolder).bind(data[position] as ClusterItem)
    }

    inner class ClusterViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: ClusterItem) {
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = "Cluster ${item.category?.name.let { it } ?: "tanpa kategori"}"

            iv_avatar_cluster.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
            ll_cluster_container.setOnClickListener { listener.onClickItem(item) }
        }
    }

    interface AdapterListener {
        fun onClickItem(item: ClusterItem)
    }
}