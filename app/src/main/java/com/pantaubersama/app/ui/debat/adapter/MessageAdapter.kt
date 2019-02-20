package com.pantaubersama.app.ui.debat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.SortedAdapter
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_RIGHT_SIDE
import com.pantaubersama.app.utils.extensions.isOdd
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message_left_side.*

/**
 * @author edityomurti on 14/02/2019 22:26
 */
class MessageAdapter : SortedAdapter<Message>() {

    var listener: MessageAdapter.AdapterListener? = null

    override val itemClass: Class<Message>
        get() = Message::class.java

    override fun compare(item1: Message, item2: Message): Int {
        return item2.createdAt.compareTo(item1.createdAt)
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        return when (viewType) {
            MESSAGE_TYPE_LEFT_SIDE -> R.layout.item_message_left_side
            MESSAGE_TYPE_RIGHT_SIDE -> R.layout.item_message_right_side
            else -> 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position.isOdd()) {
            MESSAGE_TYPE_LEFT_SIDE
        } else {
            MESSAGE_TYPE_RIGHT_SIDE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_LEFT_SIDE -> MessageViewholder(getView(parent, viewType))
            MESSAGE_TYPE_RIGHT_SIDE -> MessageViewholder(getView(parent, viewType))
            else -> MessageViewholder(getView(parent, viewType))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getData(position)?.let { (holder as MessageViewholder).bind(it) }
    }

    inner class MessageViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Message) {
            tv_content.text = item.body
            tv_clap_count.text = item.likedCount.toString()

            if (item.isLiked) {
                iv_clap.setImageResource(R.drawable.ic_clap)
            } else {
                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            cl_btn_clap.setOnClickListener { onClapClicked(item) }
        }

        fun onClapClicked(item: Message) {
            val initialClapState = item.isLiked
            item.isLiked = !item.isLiked
            if (initialClapState) {
                item.likedCount -= 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_pressed_yellow)
            } else {
                item.likedCount += 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            notifyItemChanged(adapterPosition)
        }
    }

    interface AdapterListener {
        fun onClickClap()
    }
}