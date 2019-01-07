package com.pantaubersama.app.ui.profile.setting.badge

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_badge.*
import javax.inject.Inject

class BadgeActivity : BaseActivity<BadgePresenter>(), BadgeView {

    @Inject
    lateinit var interactor: ProfileInteractor

    private lateinit var adapter: BadgeAdapter

    override fun statusBarColor(): Int? = R.color.white

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initPresenter(): BadgePresenter? {
        return BadgePresenter(interactor)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupRecyclerView()
        onClickAction()
        presenter?.refreshBadges()
    }

    private fun setupRecyclerView() {
        adapter = BadgeAdapter {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        badge_recycler_view.adapter = adapter
        badge_recycler_view.layoutManager = LinearLayoutManager(this)

        badge_swipe_refresh.setOnRefreshListener {
            presenter?.refreshBadges()
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
}
