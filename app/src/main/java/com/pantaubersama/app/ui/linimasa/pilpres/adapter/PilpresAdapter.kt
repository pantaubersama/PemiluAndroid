package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
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
import com.pantaubersama.app.data.model.tweet.PilpresTweet
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pilpres_tweet.*
import kotlinx.android.synthetic.main.layout_option_dialog_pilpres_tweet.*

/**
 * @author edityomurti on 19/12/2018 14:17
 */
class PilpresAdapter(context: Context) : BaseAdapter<PilpresTweet, PilpresAdapter.PilpresViewHolder>(context) {

    var listener: PilpresAdapter.AdapterListener? = null

    inner class PilpresViewHolder(
        override val containerView: View?,
        itemClickListener: OnItemClickListener?,
        itemLongClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<PilpresTweet>(
        containerView!!,
        itemClickListener,
        itemLongClickListener),
        LayoutContainer {

        override fun bind(item: PilpresTweet) {
            tv_tweet_content.text = item.tweetContent
            tv_tweet_content.setOnClickListener {
                listener?.onClickTweetContent(item)
            }
            iv_option.setOnClickListener {
                showOptionDialog(itemView, item)
            }
        }
    }

    override fun initViewHolder(view: View, viewType: Int): PilpresViewHolder {
        return PilpresViewHolder(view, itemClickListener, itemLongClickListener)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_pilpres_tweet
    }

    interface AdapterListener {
        fun onClickTweetContent(item: PilpresTweet)
        fun onClickShare(item: PilpresTweet)
    }

    private fun showOptionDialog(itemView: View?, item: PilpresTweet) {
        val dialog = Dialog(itemView?.context!!)
        dialog.setContentView(R.layout.layout_option_dialog_pilpres_tweet)
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
        dialog.action_copy_url?.setOnClickListener {
            val clipboard = itemView.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, "tweet id : ${item.id}")
            clipboard.primaryClip = clip
            ToastUtil.show(itemView.context, "tweet copied to clipboard")
            dialog.dismiss()
        }
        dialog.action_share?.setOnClickListener {
            listener?.onClickShare(item)
            dialog.dismiss()
        }
        dialog.action_open_in_app?.setOnClickListener {
            ChromeTabUtil(itemView.context).loadUrl("https://twitter.com/businessinsider/status/1077438036122787840")
        }
        dialog.show()
    }
}