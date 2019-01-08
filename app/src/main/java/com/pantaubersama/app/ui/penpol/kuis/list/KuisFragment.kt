package com.pantaubersama.app.ui.penpol.kuis.list

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.KuisListItem
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.penpol.kuis.ikutikuis.IkutiKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.utils.LineDividerItemDecoration
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import javax.inject.Inject

class KuisFragment : BaseFragment<KuisPresenter>(), KuisView {
    val TAG = KuisFragment::class.java.simpleName

    @Inject
    override lateinit var presenter: KuisPresenter

    private lateinit var adapter: KuisListAdapter

    override fun setLayout(): Int = R.layout.fragment_kuis

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        adapter = KuisListAdapter()
        adapter.listener = object : KuisListAdapter.AdapterListener {
            override fun onClickBanner(item: BannerInfo) {
                startActivityForResult(
                    BannerInfoActivity.setIntent(
                        context!!, PantauConstants.Extra.TYPE_KUIS, item
                    ), PantauConstants.RequestCode.BANNER_KUIS)
            }

            override fun onClickIkuti(item: KuisListItem.Item) {
                val intent = Intent(context, IkutiKuisActivity::class.java)
                intent.putExtra(PantauConstants.Kuis.KUIS_ID, item.id)
                startActivity(intent)
            }

            override fun onClickLanjut(item: KuisListItem.Item) {
                startActivity(KuisActivity.setIntent(requireContext(), item.id, 2))
            }

            override fun onClickHasil(item: KuisListItem.Item) {
                startActivity(Intent(requireContext(), KuisResultActivity::class.java))
            }

            override fun onClickShare(item: KuisListItem.Item) {
                shareKuis(item)
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(LineDividerItemDecoration(color(R.color.gray_3), dip(1), dip(16)))

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getDataList()
        }
        getDataList()
    }

    private fun getDataList() {
        presenter.getList()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
        setupData()
    }

    private fun shareKuis(item: KuisListItem.Item) {
        val targetedShareIntents: MutableList<Intent> = ArrayList()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = activity?.packageManager?.queryIntentActivities(shareIntent, 0)
        if (!resInfo!!.isEmpty()) {
            for (resolveInfo in resInfo) {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")
                sendIntent.putExtra(Intent.EXTRA_TEXT, "pantau.co.id" + "share/q/" + item.id)
                sendIntent.type = "text/plain"
                if (!resolveInfo.activityInfo.packageName.contains("pantaubersama")) {
                    sendIntent.`package` = resolveInfo.activityInfo.packageName
                    targetedShareIntents.add(sendIntent)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Bagikan dengan")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())
            startActivity(chooserIntent)
        }
    }

    private fun setupData() {
        dismissLoading()
        recycler_view.visibleIf(true)
        val dummyData: MutableList<ItemModel> = mutableListOf(
            KuisListItem.Result(70, "Jokowi - Makruf"),
            KuisListItem.Item(1, 1, 7, KuisState.NOT_TAKEN),
            KuisListItem.Item(2, 2, 7, KuisState.COMPLETED),
            KuisListItem.Item(3, 3, 7, KuisState.INCOMPLETE)
        )

        if (adapter.data.size != 0 && adapter.data[0] is BannerInfo) {
            val bannerInfo = adapter.data[0] as BannerInfo
            adapter.data.clear()
            adapter.addBanner(bannerInfo)
            adapter.data.addAll(dummyData)
        } else {
            adapter.data = dummyData
        }
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        lottie_loading.setVisible(true)
        view_empty_state.emptyStateVisible(false)
        view_fail_state.failStateVisible(false)
        recycler_view.visibleIf(false)
    }
    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.setVisible(false)
    }

    override fun showFailedGetData() {
        view_fail_state.failStateVisible(true)
    }

    companion object {
        fun newInstance(): KuisFragment {
            return KuisFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.RequestCode.FILTER_KUIS -> {
                    // refresh kuis
                }
            }
        }
    }
}
