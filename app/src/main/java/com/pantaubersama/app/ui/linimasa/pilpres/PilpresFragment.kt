package com.pantaubersama.app.ui.linimasa.pilpres

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.PilpresInteractor
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import timber.log.Timber
import javax.inject.Inject

class PilpresFragment : BaseFragment<PilpresPresenter>(), PilpresView {
    val TAG = PilpresFragment::class.java.simpleName

    @Inject
    lateinit var pilpresInteractor: PilpresInteractor

    @Inject
    lateinit var bannerInfoInteractor: BannerInfoInteractor

//    private var page = 1
    private var perPage = 20

    private lateinit var adapter: PilpresAdapter

    companion object {
//        val ARGS1 = "ARGS1"

        fun newInstance(): PilpresFragment {
            val fragment = PilpresFragment()
            val args = Bundle()
//            args.putString(ARGS1, var1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initInjection() {
        (activity?.application as BaseApp).createActivityComponent(activity)?.inject(this)
    }

    override fun initPresenter(): PilpresPresenter? {
        return PilpresPresenter(pilpresInteractor, bannerInfoInteractor)
    }

    override fun initView(view: View) {
        setupRecyclerPilpres()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }

    override fun hideBanner() {
        adapter.removeBanner()
    }

    fun setupRecyclerPilpres() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        adapter = PilpresAdapterDELDEL(context!!, isTwitterAppInstalled(), true)
        adapter = PilpresAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : PilpresAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_PILPRES), PantauConstants.RequestCode.BANNER_PILPRES)
            }

            override fun onClickTweetOption(item: FeedsItem) {
                // show option dialog
            }

            override fun onClickTweetContent(item: FeedsItem) {
                ChromeTabUtil(context!!).loadUrl(PantauConstants.Networking.BASE_TWEET_URL + item.source?.id)
//                var openTweetinBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(PantauConstants.Networking.BASE_TWEET_URL + item.source?.id))
//                startActivityForResult(openTweetinBrowser, 666)
            }

            override fun onClickShare(item: FeedsItem) {
                shareTweet(item)
            }
        }
        adapter.addSupportLoadMore(recycler_view, object : BaseRecyclerAdapter.OnLoadMoreListener {
            override fun loadMore(page: Int) {
//                this@PilpresFragment.page = page
                adapter.setLoading()
                presenter?.getFeeds(page, perPage)
            }
        }, 10)

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getFeedsData()
        }
        getFeedsData()
    }

    fun getFeedsData() {
        adapter.setDataEnd(false)
        presenter?.getList()
    }

    override fun showFeeds(feedsList: MutableList<FeedsItem>) {
        recycler_view.visibility = View.VISIBLE
        if (adapter.itemCount != 0 && adapter.get<ItemModel>(0) is BannerInfo) {
            var bannerInfo = adapter.get<BannerInfo>(0)
            adapter.clear()
            adapter.addBanner(bannerInfo)
            adapter.addData(feedsList as MutableList<ItemModel>)
            scrollToTop(false)
        } else {
            adapter.setDatas(feedsList as MutableList<ItemModel>)
        }
    }

    override fun showMoreFeeds(feedsList: MutableList<FeedsItem>) {
        adapter.setLoaded()
        if (feedsList.size < perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(feedsList as MutableList<ItemModel>)
    }

    override fun showFailedGetData() {
        // show Failed View
//        ToastUtil.show(context!!, "Gagal memuat pilpres")
    }

    override fun showEmptyData() {
        view_empty_state.visibility = View.VISIBLE
    }

    private fun shareTweet(item: FeedsItem) {
        ShareUtil.shareItem(context!!, item)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
    }

    override fun showLoading() {
        view_empty_state.visibility = View.GONE
        recycler_view.visibility = View.INVISIBLE
        lottie_loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        recycler_view.visibility = View.GONE
        lottie_loading.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
//                PantauConstants.RequestCode.BANNER_PILPRES -> hideBanner()
            }
        }
    }

    override fun onDestroy() {
        (activity?.application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }

    fun scrollToTop(smoothScroll: Boolean) {
        if (smoothScroll) {
            recycler_view.smoothScrollToPosition(0)
        } else {
            recycler_view.scrollToPosition(0)
        }
    }

    private fun isTwitterAppInstalled(): Boolean {
        val pkManager = activity?.getPackageManager()
        var isInstalled = false
        try {
            val pkgInfo = pkManager?.getPackageInfo("com.twitter.android", 0)
            val getPkgInfo = pkgInfo.toString()

            Timber.d("pkginfo : $pkgInfo")

            if (getPkgInfo.contains("com.twitter.android")) {
                isInstalled = true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

            isInstalled = false
        }
        return isInstalled
    }
}