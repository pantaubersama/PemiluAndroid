package com.pantaubersama.app.ui.penpol.kuis.list

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.KuisItem
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
                        requireContext(), PantauConstants.Extra.EXTRA_TYPE_KUIS, item
                    ), PantauConstants.RequestCode.RC_BANNER_KUIS)
            }

            override fun onClickOpenKuis(item: KuisItem) {
                val intent = when (item.state) {
                    KuisState.NOT_PARTICIPATING -> IkutiKuisActivity.setIntent(requireContext(), item)
                    KuisState.IN_PROGRESS -> KuisActivity.setIntent(requireContext(), item.id, item.title)
                    KuisState.FINISHED -> KuisResultActivity.setIntent(requireContext(), item.id, item.title)
                }
                startActivityForResult(intent, PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT)
            }

            override fun onClickShare(item: KuisItem) {
                shareKuis(item)
            }
        }

        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(LineDividerItemDecoration(color(R.color.gray_3), dip(1), dip(16)))
        adapter.addSupportLoadMore(recycler_view, 3, presenter::getNextPage)

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getTopPageItems()
        }
        getTopPageItems()
    }

    private fun getTopPageItems() {
        adapter.setDataEnd(false)
        presenter.getTopPageItems()
    }

    override fun showTopPageItems(itemModels: List<ItemModel>) {
        adapter.setDatas(itemModels)
    }

    override fun showMoreKuis(list: List<KuisItem>) {
        adapter.addData(list)
        if (list.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
    }

    override fun showLoadingMore() {
        adapter.setLoading()
    }

    override fun dismissLoadingMore() {
        adapter.setLoaded()
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(true)
        lottie_loading.enableLottie(false)
    }

    override fun showFailedGetData() {
        recycler_view.visibleIf(false)
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    private fun shareKuis(item: KuisItem) {
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

    companion object {
        fun newInstance(): KuisFragment {
            return KuisFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT -> getTopPageItems()
            }
        }
    }
}
