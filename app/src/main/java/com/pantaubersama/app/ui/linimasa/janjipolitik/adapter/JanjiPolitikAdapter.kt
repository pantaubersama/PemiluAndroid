package com.pantaubersama.app.ui.linimasa.janjipolitik.adapter

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
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.utils.ShareUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_janji_politik.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_option_dialog_tanya_kandidat.*

/**
 * @author edityomurti on 25/12/2018 21:43
 */
class JanjiPolitikAdapter(context: Context) : BaseAdapter<JanjiPolitik, JanjiPolitikAdapter.JanjiPolitikViewHolder>(context) {

    var listener: JanjiPolitikAdapter.AdapterListener? = null

    inner class JanjiPolitikViewHolder(
        override val containerView: View?,
        itemClickListener: OnItemClickListener?,
        itemLongClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<JanjiPolitik>(
        containerView!!,
        itemClickListener,
        itemLongClickListener),
        LayoutContainer {

        override fun bind(item: JanjiPolitik) {
            tv_janpol_title.text = item.title
            tv_janpol_content.text = item.content
            ll_janpol_content.setOnClickListener { listener?.onClickContent(item) }
            iv_share_button.setOnClickListener { ShareUtil(itemView.context, item) }
            iv_options_button.setOnClickListener { showOptionDialog(itemView, item) }
        }
    }

    override fun initViewHolder(view: View, viewType: Int): JanjiPolitikViewHolder {
        return JanjiPolitikViewHolder(view, itemClickListener, itemLongClickListener)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_janji_politik
    }

    interface AdapterListener {
        fun onClickContent(item: JanjiPolitik)
    }

    private fun showOptionDialog(itemView: View?, item: JanjiPolitik) {
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