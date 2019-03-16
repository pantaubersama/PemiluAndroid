package com.pantaubersama.app.ui.debat.adapter

import android.animation.ValueAnimator
import android.graphics.ColorFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
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
import kotlinx.android.synthetic.main.item_words_input_challenger.*
import kotlinx.android.synthetic.main.item_words_challenger.*

/**
 * @author edityomurti on 24/02/2019 19:48
 */
class WordsFighterAdapter(val isMyChallenge: Boolean = false) : BaseRecyclerAdapter() {

    lateinit var recyclerView: RecyclerView
    lateinit var listener: WordsFighterAdapter.AdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WORD_TYPE_CHALLENGER -> WordFighterViewholder(parent.inflate(R.layout.item_words_challenger))
            WORD_TYPE_OPPONENT -> WordFighterViewholder(parent.inflate(R.layout.item_words_opponent))
            WORD_INPUT_CHALLENGER -> WordInputViewHolder(parent.inflate(R.layout.item_words_input_challenger))
            WORD_INPUT_OPPONENT -> WordInputViewHolder(parent.inflate(R.layout.item_words_input_opponent))
            else -> throw IllegalArgumentException("unkown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? WordFighterViewholder)?.bind(data[position] as WordItem)
        (holder as? WordInputViewHolder)?.bind(data[position] as WordInputItem)
    }

    inner class WordFighterViewholder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(2000)
        fun bind(item: WordItem) {
            tv_content.text = item.body
            tv_clap_count.text = item.clapCount.toString()

            if (isMyChallenge) {
                cl_btn_clap.isEnabled = false
                val simpleColorFilter = SimpleColorFilter(itemView.context.color(R.color.gray_dark_1))
                lottie_clap.addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER, LottieValueCallback<ColorFilter>(simpleColorFilter))
            } else {
                if (item.isClap) {
                    lottie_clap.progress = 1f
                    // unclap disabled @edityo 15/3/19
                    cl_btn_clap.isEnabled = false
                    cl_btn_clap.setOnClickListener(null)
                } else {
                    lottie_clap.progress = 0f
                    cl_btn_clap.isEnabled = true
                    cl_btn_clap.setOnClickListener { if (!animator.isRunning) onClapClicked(item) }
                }
            }

            tv_posted_time.text = item.createdAt.parseDate("HH:mm")
            tv_read_estimation.text = "Estimasi baca ${item.readTime.let { if (it >= 1f) it else "<1"}} menit"
        }

        private fun onClapClicked(item: WordItem) {
            val initialClapState = item.isClap
            item.isClap = !item.isClap
            if (initialClapState) {
                item.clapCount -= 1
                lottie_clap.progress = 0f
            } else {
                item.clapCount += 1
                animator.addUpdateListener { animation ->
                    lottie_clap.progress = animation.animatedValue as Float
                }
                animator.start()
                cl_btn_clap.isEnabled = false
                listener.onClickClap(item)
            }
            tv_clap_count.text = item.clapCount.toString()
        }
    }

    inner class WordInputViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: WordInputItem) {
            enableView(item.isActive, cl_box_message as ViewGroup)

            et_content.setText(item.body)
            et_content.hint = if (!item.isActive) "Belum giliran kamu" else "Tulis argumen kamu disini"

            et_content.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
                listener.onWordsInputFocused(isFocused)
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
                    } else {
                        view_indicator_input_message.background = itemView.context.drawable(R.drawable.rounded_2dp_gray_dark_1)

                        btn_publish.setTextColor(itemView.context.color(R.color.gray_dark_1))
                        if (!btn_publish.isClickable) btn_publish.isClickable = false
                        if (!btn_cancel.isClickable) btn_cancel.isClickable = false
                    }
                    if (et_content.lineCount > et_content.minLines) {
                        if (recyclerView.canScrollVertically(1)) {
                            recyclerView.smoothScrollBy(0, recyclerView.bottom - 1)
                        }
                    }
                    item.body = query.toString()
                }
            })

            btn_publish.setOnClickListener {
                val content = et_content.text.toString()
                if (content.isNotEmpty() && content.isNotBlank()) {
                    item.body = content
                    item.isActive = false
                    et_content.clearFocus()
                    notifyItemChanged(adapterPosition)
                    listener.onPublish(content)
                } else {
                    ToastUtil.show(it.context, "isi argumenmu dulu")
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
        fun onClickClap(words: WordItem)
        fun onWordsInputFocused(isFocused: Boolean)
        fun onPublish(words: String)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addWordsInputItem(role: String, isActive: Boolean) {
        addItem(WordInputItem(role, isActive), 0)
    }

    fun clearInputMessage(isActive: Boolean = false) {
        (get(0) as? WordInputItem)?.apply {
            body = ""
            this.isActive = isActive
            notifyItemChanged(0)
        }
    }

    override fun addItem(item: ItemModel) {
        val position = if (!data.isEmpty() && data[0] is WordInputItem) 1 else 0
        if (data.find { (it as? WordItem)?.id == (item as WordItem).id } == null) {
            super.addItem(item, position)
            recyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    fun getMyTimeLeft(myRole: String): Int {
        val myLastArgumen = data.find { (it as? WordItem?)?.author?.role == myRole }
        return Math.round((myLastArgumen as WordItem).timeLeft)
    }
}