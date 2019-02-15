package com.pantaubersama.app.ui.debat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.SortedAdapter
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer

/**
 * @author edityomurti on 14/02/2019 22:26
 */
class MessageAdapter : SortedAdapter<Message>() {
    override val itemClass: Class<Message>
        get() = Message::class.java

    override fun compare(item1: Message, item2: Message): Int {
        return item2.id.compareTo(item1.id)
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        return when (viewType) {
            MESSAGE_TYPE_LEFT_SIDE -> R.layout.item_message_left_side
            else -> 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getData(position)?.getType() ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_LEFT_SIDE -> MessageViewholder(getView(parent, viewType))
            else -> MessageViewholder(getView(parent, viewType))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getData(position)?.let { (holder as MessageViewholder).bind(it) }
    }

    inner class MessageViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Message) {

        }
    }
}