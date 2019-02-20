package com.pantaubersama.app.ui.debat.adapter

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.spannable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_komentar.*
import timber.log.Timber

/**
 * @author edityomurti on 20/02/2019 13:01
 */
class KomentarAdapter : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KomentarViewHolder(parent.inflate(R.layout.item_komentar))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? KomentarViewHolder)?.bind(data[position] as Komentar)
    }

    inner class KomentarViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Komentar) {
            item.user.avatar?.medium?.url?.let { iv_komentar_avatar.loadUrl(it, R.color.gray_4) }
            Timber.d("fullName = ${item.user.fullName}")
            tv_komentar_content.text = spannable {
                item.user.fullName?.let { bold { textColor(itemView.context.color(R.color.black_2)) { +it } } }
                + "  "
                + item.content
            }.toCharSequence()
            tv_komentar_posted_time.text = item.createdAt
        }
    }

}