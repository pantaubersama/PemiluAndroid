package com.pantaubersama.app.ui.clusterdialog

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_CLUSTER_ITEM
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cluster.*
import kotlinx.android.synthetic.main.item_default_cluster_filter.*

class ClusterListDialogAdapter() : BaseRecyclerAdapter() {

    var listener: AdapterListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_DEFAULT
        } else {
            TYPE_CLUSTER_ITEM
        }
    }

    override fun setDatas(items: MutableList<ItemModel>) {
        super.setDatas(items)
        addItem(ClusterItem(), 0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DEFAULT -> DefaultViewHolder(parent.inflate(R.layout.item_default_cluster_filter))
            else -> ClusterViewHolder(parent.inflate(R.layout.item_cluster))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ClusterViewHolder)?.bind(data[position] as ClusterItem)
        (holder as? DefaultViewHolder)?.bind()
    }

    inner class ClusterViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind(item: ClusterItem) {
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = "${item.memberCount} anggota"
            iv_avatar_cluster.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
            ll_cluster_container.setOnClickListener { listener?.onClickItem(item) }
        }
    }

    inner class DefaultViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind() {
            ll_default_cluster_filter.setOnClickListener { listener?.onClickDefault() }
        }
    }

    companion object {
        private const val TYPE_DEFAULT = 0
    }

    interface AdapterListener {
        fun onClickItem(item: ClusterItem)
        fun onClickDefault()
    }
}