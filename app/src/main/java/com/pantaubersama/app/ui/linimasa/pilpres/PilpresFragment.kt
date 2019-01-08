package com.pantaubersama.app.ui.linimasa.pilpres

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.linimasa.FeedsItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.emptyStateVisible
import com.pantaubersama.app.utils.extensions.failStateVisible
import com.pantaubersama.app.utils.extensions.setVisible
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import timber.log.Timber
import javax.inject.Inject

class PilpresFragment : BaseFragment<PilpresPresenter>(), PilpresView {
    val TAG = PilpresFragment::class.java.simpleName

    @Inject
    override lateinit var presenter: PilpresPresenter

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

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupRecyclerPilpres()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }
    fun setupRecyclerPilpres() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = PilpresAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : PilpresAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_PILPRES, bannerInfo), PantauConstants.RequestCode.RC_BANNER_PILPRES)
            }

            override fun onClickTweetOption(item: FeedsItem) {
                val dialog = OptionDialog(context!!, R.layout.layout_option_dialog_pilpres_tweet)
                if (!isTwitterAppInstalled()) {
                    dialog.removeItem(R.id.action_open_in_app)
                }
                dialog.show()
                dialog.listener = object : OptionDialog.DialogListener {
                    override fun onClick(viewId: Int) {
                        when (viewId) {
                            R.id.action_copy_url -> {
                                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, "tweet id : ${item.id}")
                                clipboard.primaryClip = clip
                                ToastUtil.show(context!!, "tweet copied to clipboard")
                                dialog.dismiss()
                            }
                            R.id.action_share -> {
                                ShareUtil.shareItem(context!!, item)
                                dialog.dismiss()
                            }
                            R.id.action_open_in_app -> {
                                val openInTwitterApp = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://status?status_id=${item.source?.id}"))
                                context!!.startActivity(openInTwitterApp)
                            }
                        }
                    }
                }
            }

            override fun onClickTweetContent(item: FeedsItem) {
                ChromeTabUtil(context!!).forceLoadUrl(PantauConstants.Networking.BASE_TWEET_URL + item.source?.id)
            }

            override fun onClickShare(item: FeedsItem) {
                shareTweet(item)
            }
        }
        adapter.addSupportLoadMore(recycler_view, object : BaseRecyclerAdapter.OnLoadMoreListener {
            override fun loadMore(page: Int) {
                adapter.setLoading()
                presenter.getFeeds(page)
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
        presenter.getList()
    }

    override fun showFeeds(feedsList: MutableList<FeedsItem>) {
        recycler_view.visibleIf(true)
        if (adapter.itemCount != 0 && adapter.get(0) is BannerInfo) {
            val bannerInfo = adapter.get(0) as BannerInfo
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
        if (feedsList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(feedsList as MutableList<ItemModel>)
    }

    override fun showFailedGetData() {
        view_fail_state.failStateVisible(true)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun showEmptyData() {
        view_empty_state.emptyStateVisible(true)
    }

    private fun shareTweet(item: FeedsItem) {
        ShareUtil.shareItem(context!!, item)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
            }
        }
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