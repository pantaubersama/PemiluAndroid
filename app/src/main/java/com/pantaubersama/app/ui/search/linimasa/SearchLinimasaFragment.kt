package com.pantaubersama.app.ui.search.linimasa

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PackageCheckerUtil
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.PantauConstants.Networking.BASE_TWEET_URL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_PILPRES
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_linimasa.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchLinimasaFragment : BaseFragment<SearchLinimasaPresenter>(), SearchLinimasaView, UpdateableFragment {
    private lateinit var keyword: String

    @Inject override lateinit var presenter: SearchLinimasaPresenter
    private lateinit var adapter: PilpresAdapter

    override fun setLayout(): Int = R.layout.fragment_search_linimasa

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        private val TAG = SearchLinimasaFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchLinimasaFragment {
            val fragment = SearchLinimasaFragment()
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
        tv_filter.setOnClickListener { startActivityForResult(FilterPilpresActivity.setIntent(requireContext(), true), RC_FILTER_PILPRES) }
    }

    override fun getData(keyword: String) {
        this.keyword = keyword
        adapter.setDataEnd(false)
        presenter.getList(keyword, 1)
    }

    private fun setupRecycler() {
        adapter = PilpresAdapter(PackageCheckerUtil.isTwitterAppInstalled(requireContext()))

        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        adapter.listener = object : PilpresAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                // no banner in this section
            }

            override fun onClickTweetContent(item: FeedsItem) {
                ChromeTabUtil(requireContext()).forceLoadUrl(BASE_TWEET_URL + item.source?.id)
            }

            override fun onClickTweetOption(item: FeedsItem) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_pilpres_tweet)
                dialog.removeItem(R.id.action_copy_url) /* action copy & share dihilangkan @edityo 14/01/19 */
                dialog.removeItem(R.id.action_share)
                dialog.show()
                dialog.listener = object : OptionDialog.DialogListener {
                    override fun onClick(viewId: Int) {
                        when (viewId) {
                            R.id.action_copy_url -> {
                                CopyUtil.copyFeedsItem(context!!, item.id!!)
                                dialog.dismiss()
                            }
                            R.id.action_share -> {
                                ShareUtil.shareItem(context!!, item)
                                dialog.dismiss()
                            }
                            R.id.action_open_in_app -> {
                                val openInTwitterApp = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://status?status_id=${item.source?.id}"))
                                requireContext().startActivity(openInTwitterApp)
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onClickShare(item: FeedsItem) {
                ShareUtil.shareItem(requireContext(), item)
            }
        }

        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getList(keyword, it)
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getData(keyword)
        }
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun showFeeds(feedsList: MutableList<FeedsItem>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(feedsList)
    }

    override fun showMoreFeeds(feedsList: MutableList<FeedsItem>) {
        adapter.setLoaded()

        if (feedsList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(feedsList)
    }

    override fun showEmptyData() {
        tv_empty_state.text = getString(R.string.pencarian_tidak_ditemukan)
//        view_empty_state.enableLottie(true, lottie_empty_state)
        view_empty_state.visibleIf(true)
    }

    override fun showFailedGetData() {
//        view_fail_state.enableLottie(true, lottie_fail_state)
        view_fail_state.visibleIf(true)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    RC_FILTER_PILPRES -> {
                        getData(keyword)
                    }
                }
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
}
