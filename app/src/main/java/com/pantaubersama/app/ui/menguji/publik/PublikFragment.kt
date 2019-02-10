package com.pantaubersama.app.ui.menguji.publik

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.menguji.viewadapter.BriefDebatAdapter
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.extensions.dip
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_pager_item_menguji.*
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.layout_carousel_debat.*
import kotlinx.android.synthetic.main.layout_debat_list.*
import javax.inject.Inject

class PublikFragment : BaseFragment<PublikPresenter>(), PublikView {

    @Inject
    override lateinit var presenter: PublikPresenter

    override val isPublik by unSyncLazy { arguments?.getBoolean(ARG_PUBLIK_TAB) ?: true }

    private val debatCarouselAdapter by unSyncLazy { BriefDebatAdapter(isPublik) }
    private val debatComingAdapter by unSyncLazy { BriefDebatAdapter(false) }
    private val debatDoneAdapter by unSyncLazy { BriefDebatAdapter(false) }
    private val debatOpenAdapter by unSyncLazy { BriefDebatAdapter(false) }

    private val recyclerItemDecoration by unSyncLazy {
        OffsetItemDecoration(0, top = dip(16), ignoreFirstAndLast = true)
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
        setupTimeline()

        refreshList()
    }

    private fun refreshList() {
        presenter.getBanner()
        presenter.getDebatLive()
        presenter.getDebatComingSoon()
        presenter.getDebatDone()
        presenter.getDebatOpen()
    }

    private fun setupBanner() {
        banner_background.setImageResource(R.drawable.ic_background_base_yellow)
        iv_banner_close.setOnClickListener { rl_banner_container.visibleIf(false) }
    }

    private fun setupCarousel() {
        icon_carousel.setImageResource(R.drawable.ic_outline_live)
        text_carousel_title.text = "Live Now"
        button_more_live.setOnClickListener { }
        carousel_debat.adapter = debatCarouselAdapter
        carousel_debat.addItemDecoration(OffsetItemDecoration(dip(16), top = 0,
            orientation = RecyclerView.HORIZONTAL))
    }

    private fun setupTimeline() {
        text_timeline_label.text = "LINIMASA DEBAT"
        text_timeline_description.text = "Daftar challenge dan debat yang akan atau sudah berlangsung ditampilkan semua di sini."

        setupDebatComingSoon()
        setupDebatDone()
        setupDebatOpen()
    }

    private fun setupDebatComingSoon() {
        label_debat_coming.text = "Debat: Coming Soon"
        recycler_debat_coming.adapter = debatComingAdapter
        recycler_debat_coming.addItemDecoration(recyclerItemDecoration)
        button_more_debat_coming.setOnClickListener { }
    }

    private fun setupDebatDone() {
        label_debat_done.text = "Debat: Done"
        recycler_debat_done.adapter = debatDoneAdapter
        recycler_debat_done.addItemDecoration(recyclerItemDecoration)
        button_more_debat_done.setOnClickListener { }
    }

    private fun setupDebatOpen() {
        label_debat_open.text = "Challenge"
        recycler_debat_open.adapter = debatOpenAdapter
        recycler_debat_open.addItemDecoration(recyclerItemDecoration)
        button_more_debat_open.setOnClickListener { }
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        tv_banner_text.text = bannerInfo.body
        iv_banner_image.loadUrl(bannerInfo.headerImage?.url, R.drawable.ic_dummy_word_stadium)
    }

    override fun showDebatLive(list: List<DebatItem.LiveNow>) {
        debatCarouselAdapter.debatItems = list
    }

    override fun showDebatComingSoon(list: List<DebatItem.ComingSoon>) {
        debatComingAdapter.debatItems = list
    }

    override fun showDebatDone(list: List<DebatItem.Done>) {
        debatDoneAdapter.debatItems = list
    }

    override fun showDebatOpen(list: List<DebatItem.Open>) {
        debatOpenAdapter.debatItems = list
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    companion object {
        private const val ARG_PUBLIK_TAB = "ARG_PUBLIK_TAB"

        fun newInstance(isPublikTab: Boolean): PublikFragment {
            return PublikFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PUBLIK_TAB, isPublikTab)
                }
            }
        }
    }
}