package com.pantaubersama.app.ui.linimasa.pilpres.adapter

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pilpres_tweet.*
import kotlinx.android.synthetic.main.layout_option_dialog_pilpres_tweet.*

/**
 * @author edityomurti on 19/12/2018 14:17
 */
class PilpresAdapter(context: Context) : BaseAdapter<FeedsItem, PilpresAdapter.PilpresViewHolder>(context) {

    var listener: PilpresAdapter.AdapterListener? = null
    var VIEW_TYPE_LOADING = 0
    var VIEW_TYPE_ITEM = 1

    open inner class PilpresViewHolder(
        override val containerView: View?,
        itemClickListener: OnItemClickListener?,
        itemLongClickListener: OnItemLongClickListener?
    ) : BaseViewHolder<FeedsItem>(
        containerView!!,
        itemClickListener,
        itemLongClickListener),
        LayoutContainer {

        @SuppressLint("SetTextI18n")
        override fun bind(item: FeedsItem) {
            Glide.with(itemView.context).load(item.account?.profileImageUrl).into(iv_tweet_avatar)
            tv_tweet_name.text = item.account?.name
            tv_tweet_username.text = item.account?.username
            tv_tweet_content.text = item.source?.text
            tv_team_name.text = itemView.context.getString(R.string.txt_disematkan_dari) + item.team?.title
            tv_tweet_content.setOnClickListener {
                listener?.onClickTweetContent(item)
            }
            iv_option.setOnClickListener {
                showOptionDialog(itemView, item)
            }
        }
    }

    inner class LoadingViewHolder(
        override val containerView: View?,
        itemClickListener: OnItemClickListener?,
        itemLongClickListener: OnItemLongClickListener?
    ): PilpresViewHolder(containerView, itemClickListener, itemLongClickListener) {
        override fun bind(item: FeedsItem) {
            // do nothing
        }
    }

    override fun initViewHolder(view: View, viewType: Int): PilpresViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(view, itemClickListener, itemLongClickListener)
            else -> PilpresViewHolder(view, itemClickListener, itemLongClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].id) {
            VIEW_TYPE_LOADING.toString() -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun setItemView(viewType: Int): Int {
        return when (viewType) {
            VIEW_TYPE_LOADING -> R.layout.layout_loading
            else -> R.layout.item_pilpres_tweet
        }
    }

    interface AdapterListener {
        fun onClickTweetContent(item: FeedsItem)
        fun onClickShare(item: FeedsItem)
    }

    private fun showOptionDialog(itemView: View?, item: FeedsItem) {
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
            ChromeTabUtil(itemView.context).loadUrl(PantauConstants.Networking.BASE_TWEET_URL + item.source?.id)
        }
        dialog.show()
    }

    fun setLoading() {
        setLoading(FeedsItem(id = VIEW_TYPE_LOADING.toString()))
    }

//    fun setLoaded() {
//
//    }
}