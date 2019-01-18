package com.pantaubersama.app.ui.profile.setting.badge.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.AchievedBadge
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ACHIEVED_BADGE_ITEM
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ACHIEVED_ID
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_badge.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class DetailBadgeActivity : BaseActivity<DetailBadgePresenter>(), DetailBadgeView {

    private var achievedId: String? = null
    private var achievedBadge: AchievedBadge? = null

    @Inject override lateinit var presenter: DetailBadgePresenter
    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_detail_badge

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        fun setIntent(context: Context, achievedBadge: AchievedBadge): Intent {
            val intent = Intent(context, DetailBadgeActivity::class.java)
            intent.putExtra(EXTRA_ACHIEVED_BADGE_ITEM, achievedBadge)
            return intent
        }

        fun setIntent(context: Context, achievedId: String): Intent {
            val intent = Intent(context, DetailBadgeActivity::class.java)
            intent.putExtra(EXTRA_ACHIEVED_ID, achievedId)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(EXTRA_ACHIEVED_BADGE_ITEM)?.let { achievedBadge }
        intent.getStringExtra(EXTRA_ACHIEVED_ID)?.let { achievedId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        if (achievedBadge != null) {
            achievedBadge?.let { showBadge(it) }
        } else {
            achievedId?.let { presenter.getBadge(it) }
        }
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        ll_badge_content.visibleIf(false)
        view_fail_state.enableLottie(false, lottie_fail_state)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun showBadge(item: AchievedBadge) {
        item.user.avatar?.medium?.url?.let { iv_user_avatar.loadUrl(it) }
        item.user.fullName?.let { tv_user_fullname.text = it }
        item.user.about?.let { tv_user_bio.text = it }

        iv_badge.loadUrl(item.badge.image.thumbnail?.url, R.drawable.dummy_badge)

        tv_badge_name.text = item.badge.name
        tv_badge_desc.text = item.badge.description

        btn_share.setOnClickListener { ShareUtil.shareItem(this, item) }
    }

    override fun onFailedGetBadge(throwable: Throwable) {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }
}
