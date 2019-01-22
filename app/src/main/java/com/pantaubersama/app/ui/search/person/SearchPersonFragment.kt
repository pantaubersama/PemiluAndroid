package com.pantaubersama.app.ui.search.person

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.ui.search.person.filter.FilterOrangActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_person.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchPersonFragment : BaseFragment<SearchPersonPresenter>(), UpdateableFragment, SearchPersonView {
    private lateinit var keyword: String
    private lateinit var adapter: PersonAdapter

    override fun setLayout(): Int = R.layout.fragment_search_person

    @Inject
    override lateinit var presenter: SearchPersonPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
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

    companion object {
        private val TAG = SearchPersonFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchPersonFragment {
            val fragment = SearchPersonFragment()
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
        setupPeopleList()
        if (savedInstanceState?.getString("keyword") != null) {
            savedInstanceState.getString("keyword")?.let { getData(it) }
        } else {
            getData(keyword)
        }
        swipe_refresh.setOnRefreshListener {
            getData(keyword)
            swipe_refresh.isRefreshing = false
        }
        tv_filter.setOnClickListener {
            val intent = Intent(requireContext(), FilterOrangActivity::class.java)
            startActivityForResult(intent, PantauConstants.Search.Filter.SEARCH_ORANG_REQUEST_CODE)
        }
    }

    private fun setupPeopleList() {
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        adapter = PersonAdapter()
        adapter.listener = object : PersonAdapter.Listener {
            override fun onClickItem(profile: Profile) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        recycler_view.adapter = adapter
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.searchPerson(keyword, it, 10)
        }
    }

    override fun getData(keyword: String) {
        adapter.setDataEnd(false)
        presenter.searchPerson(keyword, 1, 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.Search.Filter.SEARCH_ORANG_REQUEST_CODE) {
                // presenter refresh
            }
        }
    }

    override fun showFailedGetDataAlert() {
        ToastUtil.show(requireContext(), "Gagal memuat data")
        view_fail_state.visibleIf(true)
    }

    override fun showFailedGetMoreDataAlert() {
        adapter.setLoaded()
        ToastUtil.show(requireContext(), "Gagal memuat lebih banyak data")
    }

    override fun bindData(users: MutableList<Profile>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(users)
    }

    override fun setDataEnd() {
        adapter.setLoaded()
        adapter.setDataEnd(true)
    }

    override fun showEmptyData() {
        view_empty_state.visibleIf(true)
    }

    override fun bindMoreData(users: MutableList<Profile>) {
        adapter.setLoaded()
        adapter.addData(users)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("keyword", keyword)
    }
}
