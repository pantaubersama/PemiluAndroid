package com.pantaubersama.app.ui.penpol.tanyakandidat.list

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
import kotlinx.android.synthetic.main.item_tanya_kandidat.view.*
import kotlinx.android.synthetic.main.post_action_layout.view.*
import kotlinx.android.synthetic.main.tanya_kandidat_options_layout.*

class TanyaKandidatAdapter(context: Context) : BaseAdapter<TanyaKandidat, TanyaKandidatAdapter.TanyaKandidatViewHolder>(context) {

    class TanyaKandidatViewHolder(
        view: View,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<TanyaKandidat>(
        view, clickListener, longClickListener) {
        override fun bind(item: TanyaKandidat) {
            itemView.user_name.text = item.user?.name
            itemView.question_time.text = item.createdAt
            itemView.upvote_count_text.text = item.upvoteCount.toString()
            itemView.user_question.text = item.question
            itemView.options_button.setOnClickListener {
                showOptionsDialog(itemView)
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