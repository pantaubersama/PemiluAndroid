package com.pantaubersama.app.ui.merayakan.perhitungan.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata.DataTPSActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_perhitungan.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class PerhitunganFragment : BaseFragment<PerhitunganPresenter>(), PerhitunganView {
    private lateinit var profile: Profile
    private lateinit var adapter: PerhitunganAdapter
    private var page = 1
    private var perPage = 20

    @Inject
    override lateinit var presenter: PerhitunganPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_perhitungan
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        presenter.getProfile()
        setupTPSData()
        presenter.getBanner()
    }

    override fun bindProfile(profile: Profile) {
        this.profile = profile
        fab_add.setOnClickListener {
            if (profile != EMPTY_PROFILE) {
                DataTPSActivity.start(requireContext(), PantauConstants.Merayakan.CREATE_PERHITUNGAN_REQUEST_CODE)
            } else {
                openLoginActivity()
            }
        }
    }

    private fun refreshItem() {
        page = 1
        adapter.setDataEnd(false)
        presenter.getPerhitunganData(page, perPage)
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
        refreshItem()
    }

    override fun bindTPSes(tpses: MutableList<TPS>) {
        recycler_view.visibleIf(true)
        if (adapter.itemCount != 0 && adapter.get(0) is BannerInfo) {
            val bannerInfo = adapter.get(0) as BannerInfo
            adapter.clear()
            adapter.addBanner(bannerInfo)
            adapter.addData(tpses as MutableList<ItemModel>)
            scrollToTop(false)
        } else {
            adapter.setDatas(tpses as MutableList<ItemModel>)
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
        fab_add.hide()
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(false)
        fab_add.show()
    }

    private fun setupTPSData() {
        adapter = PerhitunganAdapter()
        adapter.listener = object : PerhitunganAdapter.Listener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(requireContext(), bannerInfo), PantauConstants.RequestCode.RC_BANNER_PERHITUNGAN)
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            refreshItem()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun showEmptyAlert() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun showFailedGetDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    companion object {
        fun newInstance(): PerhitunganFragment {
            return PerhitunganFragment()
        }

        val TAG: String = PerhitunganFragment::class.java.simpleName
    }
}
