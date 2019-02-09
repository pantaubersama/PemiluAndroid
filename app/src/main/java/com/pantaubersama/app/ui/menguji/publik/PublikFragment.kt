package com.pantaubersama.app.ui.menguji.publik

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_pager_item_menguji.*
import kotlinx.android.synthetic.main.item_banner_container.*
import javax.inject.Inject

class PublikFragment: BaseFragment<PublikPresenter>(), PublikView {

    @Inject
    override lateinit var presenter: PublikPresenter

    override fun setLayout(): Int = R.layout.fragment_pager_item_menguji

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        banner_background.setImageResource(R.drawable.ic_background_base_yellow)
        iv_banner_close.setOnClickListener { rl_banner_container.visibleIf(false) }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            refreshList()
        }

        refreshList()
    }

    private fun refreshList() {
        presenter.getBanner()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        tv_banner_text.text = bannerInfo.body
        iv_banner_image.loadUrl(bannerInfo.headerImage?.url, R.drawable.ic_dummy_word_stadium)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}