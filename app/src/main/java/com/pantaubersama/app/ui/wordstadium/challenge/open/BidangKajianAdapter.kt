package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.wordstadium.BidangKajian
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_bidang_kajian.*

class BidangKajianAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PantauConstants.ItemModel.TYPE_BIDANG_KAJIAN) {
            BidangKajianViewHolder(parent.inflate(R.layout.item_bidang_kajian))
        } else {
            LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BidangKajianViewHolder)?.bind(data[position] as BidangKajian)
        (holder as? LoadingViewHolder)?.bind()
    }

    inner class BidangKajianViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(bidangKajian: BidangKajian) {
            nama_bidang_kajian.text = bidangKajian.bidangKajian
            itemView.setOnClickListener {
                listener?.onClick(bidangKajian)
            }
        }
    }

    interface Listener {
        fun onClick(bidangKajian: BidangKajian)
    }
}