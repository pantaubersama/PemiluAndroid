package com.pantaubersama.app.ui.debat.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.data.remote.exception.ErrorException
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

    var isLiked = false

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
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)

        lottie_love.progress = if (isLiked) 1.0f else 0.0f

        btn_like.setOnClickListener {
            if (!animator.isRunning) {
                isLiked = !isLiked
                if (isLiked) {
                    tv_like_count.text =  (tv_like_count.text.toString().toInt() + 1).toString()
                    animator.addUpdateListener { animation ->
                        lottie_love.progress = animation.animatedValue as Float
                    }
                    animator.start()
                } else {
                    tv_like_count.text =  (tv_like_count.text.toString().toInt() - 1).toString()
                    lottie_love.progress = 0.0f
                }
            }
        }

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

        val bgToolbarColor = color(when (debatItem) {
            is DebatItem.ComingSoon -> R.color.blue_2
            is DebatItem.Challenge -> R.color.yellow_2
            is DebatItem.Done -> R.color.purple_5
            else -> throw ErrorException("Progress debat tidak diketahui")
        })

        val bgHeader = when (debatItem) {
            is DebatItem.ComingSoon -> R.drawable.banner_coming_soon_fullheight
            is DebatItem.Challenge -> R.drawable.banner_challenge_big
            is DebatItem.Done -> R.drawable.banner_done_post
            else -> throw ErrorException("Progress debat tidak diketahui")
        }

        val contentDetail = layoutInflater.inflate(when (debatItem) {
            is DebatItem.ComingSoon -> R.layout.layout_content_detail_debat_coming_soon
            is DebatItem.Challenge -> R.layout.layout_content_detail_debat_coming_soon
            is DebatItem.Done -> R.layout.layout_content_detail_debat_done
            else -> throw ErrorException("Progress debat tidak diketahui")
        }, null)

        toolbar_detail_debat.setBackgroundColor(bgToolbarColor)
        cl_header.setBackgroundResource(bgHeader)
        ll_content_detail_debat.addView(contentDetail)

        ll_content_detail_debat.addView(layoutInflater.inflate(R.layout.layout_detail_debat, null))
    }
}