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
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
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
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
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
                    BannerInfoActivity.setIntent(requireContext(), PantauConstants.Extra.EXTRA_TYPE_LAPOR, banner
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
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
