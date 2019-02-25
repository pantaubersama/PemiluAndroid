package com.pantaubersama.app.ui.debat.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_DEBAT_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_centered_title.*

class DetailDebatActivity : CommonActivity() {
    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_detail_debat

    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    lateinit var debatItem: DebatItem

    companion object {
        fun setIntent(context: Context, type: DebatItem): Intent {
            val intent = Intent(context, DetailDebatActivity::class.java)
            intent.putExtra(EXTRA_DEBAT_ITEM, type)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(EXTRA_DEBAT_ITEM)?.let { debatItem = it as DebatItem }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupHeader()

        btn_back.setOnClickListener { onBackPressed() }
    }

    fun setupHeader() {
        tv_toolbar_title.text = debatItem.type
        rl_sisa_waktu.visibleIf(false, true)
        btn_more.setOnClickListener {
            optionDialog.setViewVisibility(R.id.delete_action, false)
            optionDialog.listener = View.OnClickListener {
                when (it.id) {
                    R.id.copy_link_action -> ToastUtil.show(it.context, "Salin Tautan")
                    R.id.share_action -> ToastUtil.show(it.context, "Bagikan")
                }
                optionDialog.dismiss()
            }
            optionDialog.show(supportFragmentManager, "dialog")
        }

        when (debatItem) {
            is DebatItem.ComingSoon -> {
                toolbar_detail_debat?.setBackgroundColor(color(R.color.blue_2))
                cl_header?.setBackgroundResource(R.drawable.banner_coming_soon_fullheight)
                ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_coming_soon, null))
            }
            is DebatItem.Challenge -> {
                toolbar_detail_debat?.setBackgroundColor(color(R.color.yellow_2))
                cl_header?.setBackgroundResource(R.drawable.banner_challenge_big)
                ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_coming_soon, null))
            }
            is DebatItem.Done -> {
                toolbar_detail_debat?.setBackgroundColor(color(R.color.purple_5))
                cl_header?.setBackgroundResource(R.drawable.banner_done_post)
                ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_content_detail_debat_done, null))
            }
        }

        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_detail_debat, null))
    }
}