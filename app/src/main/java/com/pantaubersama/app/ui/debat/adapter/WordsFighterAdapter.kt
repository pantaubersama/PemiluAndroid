package com.pantaubersama.app.ui.debat.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.data.model.debat.WordInputItem
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_INPUT_OPPONENT
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_CHALLENGER
import com.pantaubersama.app.utils.PantauConstants.Word.WORD_TYPE_OPPONENT
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Role
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.drawable
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.parseDate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_box_message_left.*
import kotlinx.android.synthetic.main.item_message_left_side.*

/**
 * @author edityomurti on 24/02/2019 19:48
 */
class WordsFighterAdapter : BaseRecyclerAdapter() {

    lateinit var recyclerView: RecyclerView
    lateinit var listener: WordsFighterAdapter.AdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WORD_TYPE_CHALLENGER -> WordFighterViewholder(parent.inflate(R.layout.item_message_left_side))
            WORD_TYPE_OPPONENT -> WordFighterViewholder(parent.inflate(R.layout.item_message_right_side))
            WORD_INPUT_CHALLENGER -> WordInputViewHolder(parent.inflate(R.layout.item_box_message_left))
            WORD_INPUT_OPPONENT -> WordInputViewHolder(parent.inflate(R.layout.item_box_message_right))
            else -> throw IllegalArgumentException("unkown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? WordFighterViewholder)?.bind(data[position] as WordItem)
        (holder as? WordInputViewHolder)?.bind(data[position] as WordInputItem)
    }

    inner class WordFighterViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordItem) {
            tv_content.text = item.body
            tv_clap_count.text = item.clapCount.toString()

            if (item.isClaped) {
                iv_clap.setImageResource(R.drawable.ic_clap)
            } else {
                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            cl_btn_clap.setOnClickListener { onClapClicked(item) }

            tv_posted_time.text = item.createdAt.parseDate("hh:mm")
            tv_read_estimation.text = "Estimasi baca ${item.readTime.let { if ( it > 0f) it else "<1"}} menit"
        }

        private fun onClapClicked(item: WordItem) {
            val initialClapState = item.isClaped
            item.isClaped = !item.isClaped
            if (initialClapState) {
                item.clapCount -= 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_pressed_yellow)
            } else {
                item.clapCount += 1
//                iv_clap.setImageResource(R.drawable.ic_appreciate_default)
            }
            notifyItemChanged(adapterPosition)
        }
    }

    inner class WordInputViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordInputItem) {
            enableView(item.isActive, cl_box_message as ViewGroup)

            et_content.setText(item.body)

            et_content.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
                listener.onMessageInputFocused(isFocused)
            }

            et_content.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (query?.isNotEmpty() == true && query.isNotBlank()) {
                        view_indicator_input_message.background = itemView.context.drawable(
                            if (item.role == Role.CHALLENGER) R.drawable.rounded_tosca_2 else R.drawable.rounded_red_2)

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
                item.isActive = false
                et_content.clearFocus()
                notifyItemChanged(adapterPosition)
                listener.onPublish(content)
            }

            if (!item.isActive) {
                itemView.setOnClickListener {
                    ToastUtil.show(it.context, "Belum giliran kamu")
                }
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
        fun onPublish(words: String)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addInputMessage(role: String, isActive: Boolean) {
        addItem(WordInputItem(role, isActive), 0)
    }

    fun clearInputMessage(isActive: Boolean = false) {
        (get(0) as? WordInputItem)?.apply {
            body = ""
            this.isActive = isActive
            notifyItemChanged(0)
        }
    }
}