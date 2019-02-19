package com.pantaubersama.app.ui.merayakan.rekapitulasi.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.Team
import com.pantaubersama.app.data.model.kuis.TeamPercentage
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import javax.inject.Inject

class RekapitulasiFragment : BaseFragment<RekapitulasiPresenter>(), RekapitulasiView {
    private lateinit var adapter: RekapitulasiAdapter

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
        adapter = RekapitulasiAdapter()
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
    }

    override fun showBanner(it: BannerInfo) {
        adapter.addBanner(it)
        val teamsPercentage: MutableList<TeamPercentage> = ArrayList()
        teamsPercentage.add(TeamPercentage(Team(1, "Jokowi-Ma'ruf", ""), 52f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Prabowo-Sandi", ""), 48f, 200000))
        teamsPercentage.add(TeamPercentage(Team(2, "Tidak sah", ""), 6f, 200000))
        adapter.addHeader(
            RekapitulasiData(100000, "1 mnt lalu", teamsPercentage, "Jawa Timur")
        )
        refreshItem()
    }

    private fun refreshItem() {
        adapter.setDataEnd(false)
        presenter.getRekapitulasiData()
    }

    override fun bindRekapitulasi(data: MutableList<RekapitulasiData>) {
        recycler_view.visibleIf(true)
        adapter.addData(data as MutableList<ItemModel>)
        adapter.addFooter()
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
