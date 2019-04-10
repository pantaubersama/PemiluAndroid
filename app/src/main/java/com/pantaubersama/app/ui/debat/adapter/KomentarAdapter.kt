package com.pantaubersama.app.ui.debat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.parseDate
import com.pantaubersama.app.utils.spannable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_komentar.*

/**
 * @author edityomurti on 20/02/2019 13:01
 */
class KomentarAdapter : BaseRecyclerAdapter() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KomentarViewHolder(parent.inflate(R.layout.item_komentar))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? KomentarViewHolder)?.bind(data[position] as WordItem)
    }

    inner class KomentarViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordItem) {
            item.author.avatar?.medium?.url?.let { iv_komentar_avatar.loadUrl(it, R.drawable.ic_avatar_placeholder) }
                ?: iv_komentar_avatar.setImageResource(R.drawable.ic_avatar_placeholder)
            tv_komentar_content.text = spannable {
                item.author.fullName?.let { bold { textColor(itemView.context.color(R.color.black_2)) { +it } } }
                + "  "
                + item.body
            }.toCharSequence()
            tv_komentar_posted_time.text = item.createdAt.parseDate("HH:mm")
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    override fun addItem(item: ItemModel) {
        if (data.find { (it as? WordItem)?.id == (item as WordItem).id } == null) {
            super.addItem(item)
            recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }
}