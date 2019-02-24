package com.pantaubersama.app.ui.debat.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.debat.InputMessageItem
import com.pantaubersama.app.data.model.debat.MESSAGE_INPUT_LEFT
import com.pantaubersama.app.data.model.debat.MESSAGE_INPUT_RIGHT
import com.pantaubersama.app.data.model.debat.MessageItem
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_INPUT_LEFT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_INPUT_RIGHT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_LEFT_SIDE
import com.pantaubersama.app.utils.PantauConstants.Message.MESSAGE_TYPE_RIGHT_SIDE
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.drawable
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_box_message_left.*
import kotlinx.android.synthetic.main.item_message_left_side.*

/**
 * @author edityomurti on 24/02/2019 19:48
 */
class MessageAdapter : BaseRecyclerAdapter() {

    lateinit var recyclerView: RecyclerView
    lateinit var listener: MessageAdapter.AdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_LEFT_SIDE -> MessageViewholder(parent.inflate(R.layout.item_message_left_side))
            MESSAGE_TYPE_RIGHT_SIDE -> MessageViewholder(parent.inflate(R.layout.item_message_right_side))
            MESSAGE_INPUT_LEFT_SIDE -> MessageInputViewHolder(parent.inflate(R.layout.item_box_message_left))
            MESSAGE_INPUT_RIGHT_SIDE -> MessageInputViewHolder(parent.inflate(R.layout.item_box_message_right))
            else -> throw IllegalArgumentException("unkown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MessageViewholder)?.bind(data[position] as MessageItem)
        (holder as? MessageInputViewHolder)?.bind(data[position] as InputMessageItem)
    }

    inner class MessageViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: MessageItem) {
            tv_content.text = item.body
            tv_clap_count.text = item.likedCount.toString()

            if (item.isLiked) {
                iv_clap.setImageResource(R.drawable.ic_clap)
            } else {
                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            cl_btn_clap.setOnClickListener { onClapClicked(item) }
        }

        private fun onClapClicked(item: MessageItem) {
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

    inner class MessageInputViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: InputMessageItem) {
            enableView(item.inputState == InputMessageItem.InputState.ACTIVE, itemView as ViewGroup)

            et_content.setText(item.body)

            et_content.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
                listener.onMessageInputFocused(isFocused)
            }

            et_content.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (query?.isNotEmpty() == true && query.isNotBlank()) {
                        if (item.type == InputMessageItem.Type.INPUT_LEFT_SIDE) {
                            view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_tosca_2)
                        } else {
                            view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_red_2)
                        }

                        btn_publish.setTextColor(itemView.context.color(R.color.orange_2))

                        if (!btn_publish.isClickable) btn_publish.isClickable = true
                        if (!btn_cancel.isClickable) btn_cancel.isClickable = true

                        if (et_content.lineCount > et_content.minLines) {
                            if (recyclerView.canScrollVertically(1)) {
                                recyclerView.smoothScrollBy(0, recyclerView.bottom - 1)
                            }
                        }
                    } else {
                        view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_2dp_gray_dark_1)

                        btn_publish.setTextColor(itemView.context.color(R.color.gray_dark_1))
                        if (!btn_publish.isClickable) btn_publish.isClickable = false
                        if (!btn_cancel.isClickable) btn_cancel.isClickable = false
                    }
                    item.body = query.toString()
                }
            })

            btn_publish.setOnClickListener {
                val content = et_content.text.toString()
                item.body = content
                item.inputState = InputMessageItem.InputState.INACTIVE
                et_content.clearFocus()
                notifyItemChanged(adapterPosition)
                listener.onPublish(content)
            }
        }

        fun enableView(enable: Boolean, viewGroup: ViewGroup) {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                child.isEnabled = enable
                if (child is ViewGroup) {
                    enableView(enable, child)
                }
            }
        }
    }

    interface AdapterListener {
        fun onClickClap()
        fun onMessageInputFocused(isFocused: Boolean)
        fun onPublish(content: String)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addInputMessage(type: InputMessageItem.Type) {
        val inputMessageItem = when (type) {
            InputMessageItem.Type.INPUT_LEFT_SIDE -> MESSAGE_INPUT_LEFT
            InputMessageItem.Type.INPUT_RIGHT_SIDE -> MESSAGE_INPUT_RIGHT
        }
        addItem(inputMessageItem, 0)
    }

    fun clearInputMessage(isActive: Boolean = false) {
        (get(0) as? InputMessageItem)?.apply {
            body = ""
            inputState = if (isActive) InputMessageItem.InputState.ACTIVE else InputMessageItem.InputState.INACTIVE
            notifyItemChanged(0)
        }
    }
}