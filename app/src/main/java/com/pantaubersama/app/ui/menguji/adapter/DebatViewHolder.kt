package com.pantaubersama.app.ui.menguji.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer

abstract class DebatViewHolder(
    override val containerView: View,
    private val fragmentManager: FragmentManager
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract val textDebatType: TextView
    abstract val imageAvatar1: ImageView
    abstract val imageAvatar2: ImageView
    abstract val textName1: TextView
    abstract val textName2: TextView
    abstract val textTag: TextView
    abstract val textOpponentCount: TextView
    abstract val buttonMoreOption: View

    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    open fun bind(item: DebatItem) {
        textDebatType.text = item.type
        textName1.text = item.debatDetail.name1
        textName2.text = item.debatDetail.name2
        textTag.text = item.debatDetail.tag

        textDebatType.setTextColor(containerView.context.color(
            if (item is DebatItem.Challenge && item.isDirect) R.color.black_2 else R.color.white))
        imageAvatar2.visibleIf(item !is DebatItem.Challenge || item.pendingOpponent > 0, true)

        setupOpponentCount(item)

        buttonMoreOption.setOnClickListener {
            showOptionDialog(item)
        }
    }

    private fun showOptionDialog(item: DebatItem) {
        val hideDeleteButton = item !is DebatItem.Challenge || !item.isOpen
        optionDialog.setViewVisibility(R.id.delete_action, !hideDeleteButton)
        optionDialog.listener = View.OnClickListener {
            when (it.id) {
                R.id.copy_link_action -> ToastUtil.show(it.context, "Salin Tautan")
                R.id.share_action -> ToastUtil.show(it.context, "Bagikan")
                R.id.delete_action -> ToastUtil.show(it.context, "Hapus")
            }
            optionDialog.dismiss()
        }

        optionDialog.show(fragmentManager, "dialog")
    }

    private fun setupOpponentCount(item: DebatItem) {
        val count = if (item is DebatItem.Challenge) when {
            item.pendingOpponent == 1 -> "?"
            item.pendingOpponent > 1 -> item.pendingOpponent.toString()
            else -> null
        } else null

        textOpponentCount.text = count
        textOpponentCount.visibleIf(item is DebatItem.Challenge && item.pendingOpponent > 0)
    }
}