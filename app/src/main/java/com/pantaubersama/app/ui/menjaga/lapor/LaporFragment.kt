package com.pantaubersama.app.ui.menjaga.lapor

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class LaporFragment : BaseFragment<LaporPresenter>(), LaporView {
    private lateinit var adapter: LaporAdapter

    @Inject
    override lateinit var presenter: LaporPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        fun newInstance(): LaporFragment {
            return LaporFragment()
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_lapor
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupLaporList()
        presenter.getProfile()
        presenter.getBanner()
    }

    override fun showBanner(banner: BannerInfo) {
        adapter.addBanner(banner)
//        refreshItem()
    }

    override fun showFailedGetDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun bindProfile(profile: Profile) {
        adapter.addHeader(profile)
    }

    private fun setupLaporList() {
        adapter = LaporAdapter()
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        adapter.listener = object : LaporAdapter.Listener {
            override fun onClickHeader() {
                // go to new lapor
            }

            override fun onClickBanner(banner: BannerInfo) {
                startActivityForResult(
                    BannerInfoActivity.setIntent(requireContext(), banner
                    ), PantauConstants.RequestCode.RC_BANNER_LAPOR)
            }
        }
    }

    override fun scrollToTop(smoothScroll: Boolean) {
        if (smoothScroll) {
            recycler_view.smoothScrollToPosition(0)
        } else {
            recycler_view.scrollToPosition(0)
        }
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(false)
    }
}
