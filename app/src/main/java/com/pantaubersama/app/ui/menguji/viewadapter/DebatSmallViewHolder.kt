package com.pantaubersama.app.ui.menguji.viewadapter

import android.view.View
import android.widget.TextView
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.item_debat_small.*

class DebatSmallViewHolder(view: View) : DebatViewHolder(view) {

    override val textDebatType: TextView = text_debat_type
    override val textName1: TextView = text_name_1
    override val textName2: TextView = text_name_2
    override val textTag: TextView = text_tag

    override fun bind(item: DebatItem) {
        super.bind(item)

        text_status.visibleIf(item is DebatItem.ComingSoon)

        when (item) {
            is DebatItem.ComingSoon -> text_status.text = "${item.date}  •  ${item.startEndTime}"
        }
    }
}