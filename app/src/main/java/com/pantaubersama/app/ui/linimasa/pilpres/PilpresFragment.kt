package com.pantaubersama.app.ui.linimasa.pilpres

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
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.ui.widget.imagepreview.ImagePreviewFragment
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PackageCheckerUtil
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Networking.BASE_TWEET_URL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_BANNER_PILPRES
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
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

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecyclerPilpres()
        getFeedsData()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }

    private fun setupRecyclerPilpres() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = PilpresAdapter(PackageCheckerUtil.isTwitterAppInstalled(requireContext()))
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : PilpresAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(context!!, EXTRA_TYPE_FEED, bannerInfo), RC_BANNER_PILPRES)
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

            override fun onClickTweetContent(item: FeedsItem) {
                ChromeTabUtil(requireContext()).forceLoadUrl(BASE_TWEET_URL + item.source?.id)
            }

            override fun onClickShare(item: FeedsItem) {
                ShareUtil.shareItem(requireContext(), item)
            }

            override fun onClickImage(imageUrl: String) {
                ImagePreviewFragment.show(imageUrl, childFragmentManager)
            }
        }
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getFeeds(it)
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getFeedsData()
        }
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
            adapter.addData(feedsList)
            scrollToTop(false)
        } else {
            adapter.setDatas(feedsList)
        }
    }

    override fun showMoreFeeds(feedsList: MutableList<FeedsItem>) {
        adapter.setLoaded()
        if (feedsList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(feedsList)
    }

    override fun showFailedGetData() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun showEmptyData() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
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