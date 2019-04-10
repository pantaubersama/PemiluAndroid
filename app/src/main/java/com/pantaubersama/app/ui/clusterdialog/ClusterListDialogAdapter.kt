package com.pantaubersama.app.ui.clusterdialog

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_CLUSTER_ITEM
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cluster.*
import kotlinx.android.synthetic.main.item_default_dropdown_filter.*

class ClusterListDialogAdapter : BaseRecyclerAdapter() {

    var listener: AdapterListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_DEFAULT
        } else {
            TYPE_CLUSTER_ITEM
        }
    }

    override fun setDatas(items: List<ItemModel>) {
        super.setDatas(items)
        addItem(ClusterItem(), 0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DEFAULT -> DefaultViewHolder(parent.inflate(R.layout.item_default_dropdown_filter))
            TYPE_CLUSTER_ITEM -> ClusterViewHolder(parent.inflate(R.layout.item_cluster))
            else -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ClusterViewHolder)?.bind(data[position] as ClusterItem)
        (holder as? DefaultViewHolder)?.bind()
        (holder as? LoadingViewHolder)?.bind()
    }

    inner class ClusterViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: ClusterItem) {
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = "${item.memberCount?.let { it } ?: "belum ada"} anggota"

            iv_avatar_cluster.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
            ll_cluster_container.setOnClickListener { listener?.onClickItem(item) }
        }
    }

    inner class DefaultViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind() {
            tv_cluster_or_party_placeholder.text = itemView.context.getString(R.string.txt_semua_cluster)
            ll_default_filter.setOnClickListener { listener?.onClickDefault() }
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