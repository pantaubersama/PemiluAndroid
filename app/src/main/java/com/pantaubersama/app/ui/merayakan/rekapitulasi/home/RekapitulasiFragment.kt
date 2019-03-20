package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah.RekapitulasiDaerahActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_fail_state.*
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

            override fun onClickItem(item: Percentage) {
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
        adapter.addItem(data as ItemModel)
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
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
