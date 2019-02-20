package com.pantaubersama.app.ui.menguji.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.menguji.adapter.BriefDebatAdapter
import com.pantaubersama.app.ui.menguji.list.DebatListActivity
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PERSONAL_CHALLENGE
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PERSONAL_CHALLENGE_IN_PROGRESS
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PERSONAL_COMING_SOON
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PERSONAL_DONE
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PUBLIK_CHALLENGE
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PUBLIK_COMING_SOON
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PUBLIK_DONE
import com.pantaubersama.app.utils.PantauConstants.Debat.Title.PUBLIK_LIVE_NOW
import com.pantaubersama.app.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_menguji_pager.*
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.layout_carousel_debat.*
import kotlinx.android.synthetic.main.layout_debat_list.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class MengujiPagerFragment : BaseFragment<MengujiPresenter>(), MengujiView {

    @Inject
    override lateinit var presenter: MengujiPresenter

    override val isPublik by unSyncLazy { arguments?.getBoolean(ARG_PUBLIK_TAB) ?: true }

    private val debatCarouselAdapter by unSyncLazy { BriefDebatAdapter(true) }
    private val debatComingAdapter by unSyncLazy { BriefDebatAdapter(false) }
    private val debatDoneAdapter by unSyncLazy { BriefDebatAdapter(false) }
    private val debatOpenAdapter by unSyncLazy { BriefDebatAdapter(false) }

    override fun setLayout(): Int = R.layout.fragment_menguji_pager

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
        if (isPublik) presenter.getDebatLive()
        presenter.getDebatComingSoon()
        presenter.getDebatDone()
        presenter.getDebatOpen()
    }

    private fun setupBanner() {
        banner_background.setImageResource(R.drawable.ic_background_base_yellow)
        iv_banner_close.setOnClickListener { hideBanner() }
    }

    private fun setupCarousel() {
        icon_carousel.setImageResource(if (isPublik) R.drawable.ic_debat_live else R.drawable.ic_debat_open)
        text_carousel_title.text = if (isPublik) "Live Now" else "Challenge in Progress"
        carousel_debat.adapter = debatCarouselAdapter
        carousel_debat.addItemDecoration(OffsetItemDecoration(dip(16), top = 0,
            orientation = RecyclerView.HORIZONTAL))
        button_more_live.setOnClickListener {
            DebatListActivity.start(requireContext(), if (isPublik) PUBLIK_LIVE_NOW else PERSONAL_CHALLENGE_IN_PROGRESS)
        }
    }

    private fun setupTimeline() {
        text_timeline_label.text = if (isPublik) "LINIMASA DEBAT" else "MY WORDSTADIUM"
        text_timeline_description.text = if (isPublik)
            "Daftar challenge dan debat yang akan atau sudah berlangsung ditampilkan semua di sini."
        else
            "Daftar tantangan dan debat yang akan atau sudah kamu ikuti ditampilkan semua di sini."

        setupDebatComingSoon()
        setupDebatDone()
        setupDebatOpen()
    }

    private fun setupDebatComingSoon() {
        label_debat_coming.text = if (isPublik) "Debat: Coming Soon" else "My Debat: Coming Soon"
        recycler_debat_coming.adapter = debatComingAdapter
        button_more_debat_coming.setOnClickListener {
            DebatListActivity.start(requireContext(), if (isPublik) PUBLIK_COMING_SOON else PERSONAL_COMING_SOON)
        }
    }

    private fun setupDebatDone() {
        label_debat_done.text = if (isPublik) "Debat: Done" else "My Debat: Done"
        recycler_debat_done.adapter = debatDoneAdapter
        button_more_debat_done.setOnClickListener {
            DebatListActivity.start(requireContext(), if (isPublik) PUBLIK_DONE else PERSONAL_DONE)
        }
    }

    private fun setupDebatOpen() {
        label_debat_open.text = if (isPublik) "Challenge" else "My Challenge"
        recycler_debat_open.adapter = debatOpenAdapter
        button_more_debat_open.setOnClickListener {
            DebatListActivity.start(requireContext(), if (isPublik) PUBLIK_CHALLENGE else PERSONAL_CHALLENGE)
        }
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        rl_banner_container.visibleIf(true)
        tv_banner_text.text = bannerInfo.body
        iv_banner_image.loadUrl(bannerInfo.headerImage?.url, R.drawable.ic_dummy_word_stadium)
        rl_banner_container.setOnClickListener {
            startActivity(BannerInfoActivity.setIntent(requireContext(), 0, bannerInfo))
        }
    }

    override fun hideBanner() {
        rl_banner_container.visibleIf(false)
    }

    override fun showDebatLive(list: List<DebatItem.LiveNow>) {
        if (isPublik) debatCarouselAdapter.debatItems = list
    }

    override fun showDebatComingSoon(list: List<DebatItem.ComingSoon>) {
        debatComingAdapter.debatItems = list
    }

    override fun showDebatDone(list: List<DebatItem.Done>) {
        debatDoneAdapter.debatItems = list
    }

    override fun showDebatOpen(list: List<DebatItem.Challenge>) {
        if (!isPublik) debatCarouselAdapter.debatItems = list
        debatOpenAdapter.debatItems = list
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        swipe_refresh.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        swipe_refresh.visibleIf(true)
    }

    companion object {
        private const val ARG_PUBLIK_TAB = "ARG_PUBLIK_TAB"

        fun newInstance(isPublikTab: Boolean): MengujiPagerFragment {
            return MengujiPagerFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PUBLIK_TAB, isPublikTab)
                }
            }
        }
    }
}