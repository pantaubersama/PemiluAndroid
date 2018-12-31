package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
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
import com.pantaubersama.app.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.item_tanya_kandidat.view.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_option_dialog_tanya_kandidat.*

class TanyaKandidatAdapter(context: Context) : BaseAdapter<Pertanyaan, TanyaKandidatAdapter.TanyaKandidatViewHolder>(context) {
    var listener: TanyaKandidatAdapter.AdapterListener? = null

    inner class TanyaKandidatViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<Pertanyaan>(
        containerView!!, clickListener, longClickListener), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bind(item: Pertanyaan) {
            GlideApp
                .with(itemView.context)
                .load(item.user?.avatar?.url)
                .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ic_person))
                .into(itemView.user_avatar)
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

    override fun initViewHolder(view: View, viewType: Int): TanyaKandidatViewHolder {
        return TanyaKandidatViewHolder(view, itemClickListener, itemLongClickListener)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_tanya_kandidat
    }

    interface AdapterListener {
        fun onClickShare(item: Pertanyaan)
    }
}