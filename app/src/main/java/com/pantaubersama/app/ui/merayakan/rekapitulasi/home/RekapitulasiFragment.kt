package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.rekapitulasi.Rekapitulasi
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah.RekapitulasiDaerahActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class RekapitulasiFragment : BaseFragment<RekapitulasiPresenter>(), RekapitulasiView {
    private lateinit var adapter: RekapitulasiComplexAdapter

    @Inject
    override lateinit var presenter: RekapitulasiPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_rekapitulasi
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRekapitulasi()
        presenter.getBanner()
    }

    private fun setupRekapitulasi() {
        adapter = RekapitulasiComplexAdapter()
        adapter.listener = object : RekapitulasiComplexAdapter.Listener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(requireContext(), bannerInfo), PantauConstants.RequestCode.RC_BANNER_REKAPITULASI)
            }

            override fun onClickItem(item: Rekapitulasi) {
                RekapitulasiDaerahActivity.start(requireContext(), "provinsi")
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            refreshItem()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun showBanner(it: BannerInfo) {
        adapter.addBanner(it)
        presenter.getTotalParticipant()
        refreshItem()
    }

    override fun showFailedGetTotalParticipantAlert() {
        ToastUtil.show(requireContext(), "Gagal memuat data total partisipan")
    }

    override fun bindTotalParticipantData(totalParticipantData: TotalParticipantData) {
        adapter.addItem(totalParticipantData as ItemModel)
        presenter.getRekapitulasiNasional()
    }

    override fun showFailedGetRekapitulasiNasionalAlert() {
        ToastUtil.show(requireContext(), "Gagal memuat data rekapitulasi")
    }

    private fun refreshItem() {
        adapter.setDataEnd(false)
        presenter.getRekapitulasiData()
    }

    override fun bindRekapitulasiNasional(data: Percentage) {
        adapter.addRekapitulasiHeader(data)
        presenter.getRekapitulasiData()
    }

    override fun bindRekapitulasiList(rekapitulasi: MutableList<Rekapitulasi>) {
        recycler_view.visibleIf(true)
        adapter.addData(rekapitulasi as MutableList<ItemModel>)
        adapter.addFooter()
    }

    override fun showFailedLoadRekapitulasiList() {
        ToastUtil.show(requireContext(), "Gagal memuat list data rekapitulasi")
    }

    override fun showFailedGetBannerAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    companion object {
        fun newInstance(): RekapitulasiFragment {
            return RekapitulasiFragment()
        }

        val TAG: String = RekapitulasiFragment::class.java.simpleName
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
