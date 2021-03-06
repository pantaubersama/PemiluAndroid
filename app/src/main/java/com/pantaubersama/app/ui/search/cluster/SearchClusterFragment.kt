package com.pantaubersama.app.ui.search.cluster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.clusterdetail.ClusterDetailActivity
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.ui.search.cluster.filter.FilterClusterActivity
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_CLUSTER
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_cluster.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchClusterFragment : BaseFragment<SearchClusterPresenter>(), SearchClusterView, UpdateableFragment {
    private lateinit var keyword: String

    @Inject override lateinit var presenter: SearchClusterPresenter
    private lateinit var adapter: ClusterAdapter

    override fun setLayout(): Int = R.layout.fragment_search_cluster

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        private val TAG = SearchClusterFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchClusterFragment {
            val fragment = SearchClusterFragment()
            val args = Bundle()
            args.putString(EXTRA_SEARCH_KEYWORD, keyword)
            fragment.arguments = args
            return fragment
        }
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString(EXTRA_SEARCH_KEYWORD)?.let { keyword = it }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        getData(keyword)

        tv_filter.setOnClickListener { startActivityForResult(Intent(requireContext(), FilterClusterActivity::class.java), RC_FILTER_CLUSTER) }
    }

    override fun getData(keyword: String) {
        this.keyword = keyword
        adapter.setDataEnd(false)
        presenter.getClusterList(keyword, 1)
    }

    private fun setupRecycler() {
        adapter = ClusterAdapter(object : ClusterAdapter.AdapterListener {
            override fun onClickItem(item: ClusterItem) {
                item.id?.let { ClusterDetailActivity.start(requireContext(), it) }
            }
        })
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getClusterList(keyword, it)
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getData(keyword)
        }
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false)
    }

    override fun showClusters(clusterList: MutableList<ClusterItem>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(clusterList)
    }

    override fun showMoreClusters(clusterList: MutableList<ClusterItem>) {
        adapter.setLoaded()
        if (clusterList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(clusterList)
    }

    override fun showEmptyData() {
        tv_empty_state.text = getString(R.string.pencarian_tidak_ditemukan)
//        view_empty_state.enableLottie(true, lottie_empty_state)
        view_empty_state.visibleIf(true)
    }

    override fun onFailedGetData(throwable: Throwable) {
//        view_fail_state.enableLottie(true, lottie_fail_state)
        view_fail_state.visibleIf(true)
    }

    override fun onFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    RC_FILTER_CLUSTER -> { getData(keyword) }
                }
            }
        }
    }
}
