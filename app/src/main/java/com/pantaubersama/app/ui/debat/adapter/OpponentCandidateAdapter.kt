package com.pantaubersama.app.ui.debat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.debat.Audience
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_debat_participant.*

/**
 * @author edityomurti on 04/03/2019 15:56
 */

class OpponentCandidateAdapter(var showConfirmButton: Boolean = false, var enableButton: Boolean = true) : BaseRecyclerAdapter() {

    lateinit var listener: AdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OpponentCandidateViewHolder(parent.inflate(R.layout.item_debat_participant))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OpponentCandidateViewHolder).bind(data[position] as Audience)
    }

    inner class OpponentCandidateViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Audience) {
            iv_avatar.loadUrl(item.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
            tv_name.text = item.fullName
            tv_username.text = item.username
            btn_confirm.visibleIf(showConfirmButton)
            if (btn_confirm.isVisible()) {
                btn_confirm.setOnClickListener {
                    listener.onClickConfirm(item)
                }
                btn_confirm.isEnabled = enableButton
            }
        }
    }

    interface AdapterListener {
        fun onClickConfirm(audience: Audience)
    }
}