package com.pantaubersama.app.ui.menguji

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_carousel_debat.*

class CarouselDebatViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: DebatItem) {
        icon_debat_type.visibleIf(item is DebatItem.LiveNow)
        text_debat_type.text = item.type
        text_name_1.text = item.debatDetail.name1
        text_name_2.text = item.debatDetail.name2
        text_tag.text = item.debatDetail.tag
    }
}