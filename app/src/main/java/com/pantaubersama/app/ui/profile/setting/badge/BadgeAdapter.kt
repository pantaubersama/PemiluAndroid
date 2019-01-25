package com.pantaubersama.app.ui.profile.setting.badge

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.setGrayScale
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_badge.*

class BadgeAdapter(private val onShareClick: (Badge) -> Unit)
    : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    var items: List<Badge> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_badge)) {
            onShareClick(items[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(override val containerView: View, onShareClick: (Int) -> Unit)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            item_badge_share_button.setOnClickListener { onShareClick(adapterPosition) }
        }

        fun bind(item: Badge) {
            item_badge_icon.loadUrl(item.image.thumbnail?.url, R.drawable.dummy_badge)
            item_badge_icon.setGrayScale(!item.achieved)
            item_badge_title.text = item.name
            item_badge_title.isEnabled = item.achieved
            item_badge_desc.text = item.description
            item_badge_desc.isEnabled = item.achieved
            item_badge_share_button.visibleIf(item.achieved)
        }
    }
}