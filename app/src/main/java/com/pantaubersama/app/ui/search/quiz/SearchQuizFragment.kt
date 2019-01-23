package com.pantaubersama.app.ui.search.quiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.ikutikuis.IkutiKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisListAdapter
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_quiz.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchQuizFragment : BaseFragment<SearchQuizPresenter>(), UpdateableFragment, SearchQuizView {
    private lateinit var keyword: String

    override fun setLayout(): Int = R.layout.fragment_search_quiz

    @Inject
    override lateinit var presenter: SearchQuizPresenter

    private lateinit var adapter: KuisListAdapter

    companion object {
        private val TAG = SearchQuizFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchQuizFragment {
            val fragment = SearchQuizFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_KEYWORD, keyword)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString(EXTRA_SEARCH_KEYWORD)?.let { keyword = it }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        getData(keyword)
        tv_filter.setOnClickListener {
            val intent = Intent(context, FilterKuisActivity::class.java)
            startActivityForResult(intent, PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT)
        }
    }

    private fun setupRecycler() {
        adapter = KuisListAdapter()
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        adapter.listener = object : KuisListAdapter.AdapterListener {
            override fun onClickBanner(item: BannerInfo) {}
            override fun onClickOpenKuis(item: KuisItem) {
                if (presenter.isLoggedIn) {
                    val intent = when (item.state) {
                        KuisState.NOT_PARTICIPATING -> IkutiKuisActivity.setIntent(requireContext(), item)
                        KuisState.IN_PROGRESS -> KuisActivity.setIntent(requireContext(), item)
                        KuisState.FINISHED -> KuisResultActivity.setIntent(requireContext(), item)
                    }
                    startActivityForResult(intent, PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT)
                } else {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            }

            override fun onClickShare(item: KuisItem) {
                ShareUtil.shareItem(requireContext(), item)
            }
        }

        adapter.addSupportLoadMore(recycler_view, 5) {
            presenter.getNextPage(keyword)
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getData(keyword)
        }
    }

    override fun getData(keyword: String) {
        this.keyword = keyword
        adapter.setDataEnd(false)
        adapter.clear()
        presenter.getFirstPage(keyword)
    }

    override fun showEmptyData() {
        tv_empty_state.text = getString(R.string.pencarian_tidak_ditemukan)
        view_empty_state.visibleIf(true)
        recycler_view.visibleIf(false)
    }

    override fun showFirstPage(list: List<KuisItem>) {
        adapter.setDatas(list)
    }

    override fun showMoreData(list: List<KuisItem>) {
        adapter.addData(list)
        view_empty_state.visibleIf(false)
        recycler_view.visibleIf(true)
    }

    override fun showFailedGetData() {
        recycler_view.visibleIf(false)
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(true)
    }

    override fun showError(throwable: Throwable) {
    }

    override fun showLoadingMore() {
        adapter.setLoading()
    }

    override fun dismissLoadingMore() {
        adapter.setLoaded()
    }

    override fun setNoMoreData() {
        adapter.setDataEnd(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT -> getData(keyword)
            }
        }
    }
}
