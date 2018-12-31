package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_tanya_kandidat_header.*
import kotlinx.android.synthetic.main.layout_option_dialog_tanya_kandidat.*

class TanyaKandidatAdapter(val mContext: Context) : BaseAdapter<Pertanyaan, TanyaKandidatAdapter.TanyaKandidatViewHolder>(mContext) {
    var listener: TanyaKandidatAdapter.AdapterListener? = null
    var VIEW_TYPE_LOADING = 0
    var VIEW_TYPE_HEADER = 1
    var VIEW_TYPE_ITEM = 2

    abstract inner class TanyaKandidatViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<Pertanyaan>(
        containerView!!, clickListener, longClickListener), LayoutContainer {

        @SuppressLint("SetTextI18n")
        protected fun showItem(item: Pertanyaan) {
            GlideApp
                .with(itemView.context)
                .load(item.user?.avatar?.url)
                .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ic_person))
                .into(user_avatar)
            tv_user_name.text = item.user?.firstName + item.user?.lastName
            question_time.text = item.createdAt?.id
            upvote_count_text.text = item.likeCount.toString()
            user_question.text = item.body
            iv_options_button.setOnClickListener {
                showOptionsDialog(itemView)
            }
            upvote_button.setOnClickListener {
                setUpvoted(item)
            }
            iv_share_button.setOnClickListener {
                listener?.onClickShare(item)
            }
        }

        protected fun showLoading() {
            // no need to do
        }

        protected fun showHeader() {
            question_section.setOnClickListener {
                val intent = Intent(mContext, CreateTanyaKandidatActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }

        private fun setUpvoted(item: Pertanyaan?) {
            val upVoted = item?.isliked
            item?.isliked = !item?.isliked!!
            val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
            if (upVoted!!) {
                val upVoteCount = item.likeCount!! - 1
                item.likeCount = upVoteCount
                upvote_count_text.text = item.likeCount.toString()
                upvote_button.progress = 0.0f
            } else {
                val loveCount = item.likeCount!! + 1
                item.likeCount = loveCount
                animator.addUpdateListener {
                    animation -> upvote_button.progress = animation.animatedValue as Float
                    upvote_count_text.text = item.likeCount.toString()
                }
                animator.start()
            }
        }

        private fun showOptionsDialog(itemView: View?) {
            val dialog = Dialog(itemView?.context!!)
            dialog.setContentView(R.layout.layout_option_dialog_tanya_kandidat)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setOnKeyListener { dialogInterface, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                    true
                } else {
                    false
                }
            }
            dialog.setCanceledOnTouchOutside(true)
            val lp = WindowManager.LayoutParams()
            val window = dialog.window
            lp.copyFrom(window?.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = lp
            lp.gravity = Gravity.BOTTOM
            window?.attributes = lp
            dialog.delete_tanya_kandidat_item_action?.setOnClickListener {
                // delete
            }
            dialog.copy_url_tanya_kandidat_action?.setOnClickListener {
                // copy url
            }
            dialog.share_tanya_kandidat_action?.setOnClickListener {
                // share
            }
            dialog.report_tanya_kandidat_action?.setOnClickListener {
                // lapor
            }
            dialog.show()
        }
    }

    inner class LoadingViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : TanyaKandidatViewHolder(containerView!!, clickListener, longClickListener), LayoutContainer {
        override fun bind(item: Pertanyaan) {
            showLoading()
        }
    }

    inner class HeaderViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : TanyaKandidatViewHolder(containerView!!, clickListener, longClickListener), LayoutContainer {
        override fun bind(item: Pertanyaan) {
            showHeader()
        }
    }

    inner class ItemViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : TanyaKandidatViewHolder(containerView!!, clickListener, longClickListener), LayoutContainer {
        override fun bind(item: Pertanyaan) {
            showItem(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].viewType) {
            VIEW_TYPE_LOADING -> VIEW_TYPE_LOADING
            VIEW_TYPE_HEADER -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun initViewHolder(view: View, viewType: Int): TanyaKandidatViewHolder? {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(view, itemClickListener, itemLongClickListener)
            VIEW_TYPE_HEADER -> HeaderViewHolder(view, itemClickListener, itemLongClickListener)
            VIEW_TYPE_ITEM -> ItemViewHolder(view, itemClickListener, itemLongClickListener)
            else -> null
        }
    }

    override fun setItemView(viewType: Int): Int? {
        return when (viewType) {
            VIEW_TYPE_LOADING -> R.layout.layout_loading
            VIEW_TYPE_HEADER -> R.layout.layout_tanya_kandidat_header
            VIEW_TYPE_ITEM -> R.layout.item_tanya_kandidat
            else -> null
        }
    }

    override fun onBindViewHolder(holder: TanyaKandidatViewHolder, position: Int) {
        when (holder) {
            is LoadingViewHolder -> (holder as LoadingViewHolder).bind(get(position))
            is HeaderViewHolder -> (holder as HeaderViewHolder).bind(get(position))
            is ItemViewHolder -> (holder as ItemViewHolder).bind(get(position))
        }
    }

    fun addHeader() {
        data.add(0, Pertanyaan(viewType = 1))
        notifyItemInserted(0)
    }

    interface AdapterListener {
        fun onClickShare(item: Pertanyaan)
    }
}