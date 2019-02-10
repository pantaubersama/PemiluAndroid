package com.pantaubersama.app.ui.menguji.publik

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.menguji.CarouselDebatAdapter
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.extensions.dip
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_pager_item_menguji.*
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.layout_carousel_debat.*
import javax.inject.Inject

class PublikFragment : BaseFragment<PublikPresenter>(), PublikView {

    @Inject
    override lateinit var presenter: PublikPresenter

    private val carouselAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CarouselDebatAdapter()
    }

    override fun setLayout(): Int = R.layout.fragment_pager_item_menguji

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            refreshList()
        }

        setupBanner()
        setupCarousel()

        refreshList()
    }

    private fun setupBanner() {
        banner_background.setImageResource(R.drawable.ic_background_base_yellow)
        iv_banner_close.setOnClickListener { rl_banner_container.visibleIf(false) }
    }

    private fun setupCarousel() {
        icon_carousel.setImageResource(R.drawable.ic_outline_live)
        text_carousel_title.text = "Live Now"
        button_see_more.setOnClickListener { }
        carousel_debat.adapter = carouselAdapter
        carousel_debat.addItemDecoration(OffsetItemDecoration(dip(16), top = 0,
            orientation = RecyclerView.HORIZONTAL))
    }

    private fun refreshList() {
        presenter.getBanner()
        presenter.getLiveDebat()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        tv_banner_text.text = bannerInfo.body
        iv_banner_image.loadUrl(bannerInfo.headerImage?.url, R.drawable.ic_dummy_word_stadium)
    }

    override fun showLiveDebat(list: List<DebatItem.LiveNow>) {
        carouselAdapter.debatItems = list
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}