package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.post_action_layout.*
import kotlinx.android.synthetic.main.tanya_kandidat_options_layout.*

class TanyaKandidatAdapter(context: Context) : BaseAdapter<TanyaKandidat, TanyaKandidatAdapter.TanyaKandidatViewHolder>(context) {

    class TanyaKandidatViewHolder(
        override val containerView: View?,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<TanyaKandidat>(
        containerView!!, clickListener, longClickListener), LayoutContainer {
        override fun bind(item: TanyaKandidat) {
            user_name.text = item.user?.name
            question_time.text = item.createdAt
            upvote_count_text.text = item.upVoteCount.toString()
            user_question.text = item.question
            options_button.setOnClickListener {
                showOptionsDialog(itemView)
            }
            upvote_button.setOnClickListener {
                setUpvoted(item)
            }
        }

        private fun setUpvoted(item: TanyaKandidat?) {
            val upVoted = item?.isUpvoted
            item?.isUpvoted = !item?.isUpvoted!!
            val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
            if (upVoted!!) {
                val upVoteCount = item.upVoteCount!! - 1
                item.upVoteCount = upVoteCount
                upvote_count_text.text = item.upVoteCount.toString()
                upvote_button.progress = 0.0f
            } else {
                val loveCount = item.upVoteCount!! + 1
                item.upVoteCount = loveCount
                animator.addUpdateListener {
                    animation -> upvote_button.progress = animation.animatedValue as Float
                    upvote_count_text.text = item.upVoteCount.toString()
                }
                animator.start()
            }
        }

        private fun showOptionsDialog(itemView: View?) {
            val dialog = Dialog(itemView?.context!!)
            dialog.setContentView(R.layout.tanya_kandidat_options_layout)
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
}