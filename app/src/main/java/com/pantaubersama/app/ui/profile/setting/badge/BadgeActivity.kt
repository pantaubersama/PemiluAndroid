package com.pantaubersama.app.ui.profile.setting.badge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_badge.*
import javax.inject.Inject

class BadgeActivity : BaseActivity<BadgePresenter>(), BadgeView {

    @Inject
    override lateinit var presenter: BadgePresenter

    private lateinit var adapter: BadgeAdapter
    private var userId: String? = null

    override fun statusBarColor(): Int? = R.color.white

    override fun fetchIntentExtra() {
        userId = intent.getStringExtra(PantauConstants.Profile.USER_ID)
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupRecyclerView()
        onClickAction()
        if (userId != null) {
            userId?.let { presenter.getUserBadges(it) }
        } else {
            presenter.refreshBadges()
        }
    }

    private fun setupRecyclerView() {
        adapter = BadgeAdapter {
            it.achievedId?.let { achievedId -> startActivity(DetailBadgeActivity.setIntent(this, achievedId))
            }
        }
        badge_recycler_view.adapter = adapter
        badge_recycler_view.layoutManager = LinearLayoutManager(this)

        badge_swipe_refresh.setOnRefreshListener {
            if (userId != null) {
                userId?.let { presenter.getUserBadges(it) }
            } else {
                presenter.refreshBadges()
            }
            badge_swipe_refresh.isRefreshing = false
        }
    }

    private fun onClickAction() {
        badge_close.setOnClickListener {
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_badge
    }

    override fun showBadges(badges: List<Badge>) {
        adapter.items = badges
    }

    override fun showLoading() {
        badge_progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        badge_progress_bar.visibleIf(false)
    }

    companion object {
        fun start(context: Context, userId: String? = null) {
            val intent = Intent(context, BadgeActivity::class.java)
            if (userId != null) {
                intent.putExtra(PantauConstants.Profile.USER_ID, userId)
            }
            context.startActivity(intent)
        }
    }
}
