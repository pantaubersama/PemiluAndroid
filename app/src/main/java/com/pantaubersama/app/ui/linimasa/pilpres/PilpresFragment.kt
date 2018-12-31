package com.pantaubersama.app.ui.linimasa.pilpres

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.data.interactors.PilpresInteractor
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tweet.PilpresTweet
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import kotlinx.android.synthetic.main.fragment_pilpres.*
import kotlinx.android.synthetic.main.layout_banner_container.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import javax.inject.Inject

class PilpresFragment : BaseFragment<PilpresPresenter>(), PilpresView {
    val TAG = PilpresFragment::class.java.simpleName

    @Inject
    lateinit var interactor: PilpresInteractor

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
        return PilpresPresenter(interactor)
    }

    override fun initView(view: View) {
        setupBanner()
        setupRecyclerPilpres()
    }

    private fun setupBanner() {
        presenter?.isBannerShown()
    }

    override fun showBanner() {
        layout_banner_pilpres.visibility = View.VISIBLE
        tv_banner_text.text = getString(R.string.pilpres_banner_text)
        iv_banner_image.setImageResource(R.drawable.ic_banner_pilpres)
        fl_banner.setOnClickListener {
            startActivityForResult(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_PILPRES), PantauConstants.RequestCode.BANNER_PILPRES)
        }
        iv_banner_close.setOnClickListener {
            layout_banner_pilpres.visibility = View.GONE
        }
    }

    override fun hideBanner() {
        layout_banner_pilpres.visibility = View.GONE
    }

    private fun setupRecyclerPilpres() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = PilpresAdapter(context!!)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
            }
        })
        adapter.listener = object : PilpresAdapter.AdapterListener {
            override fun onClickTweetContent(item: PilpresTweet) {
                ChromeTabUtil(context!!).loadUrl("https://twitter.com/AndroidDev/status/1077232436164153345")
            }

            override fun onClickShare(item: PilpresTweet) {
                shareTweet(item)
            }
        }
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getPilpresTweet()
        }
        getPilpresTweet()
    }

    fun getPilpresTweet() {
        presenter?.getPilpresTweet()
    }

    override fun showPilpresTweet(tweetList: List<PilpresTweet>) {
        if (tweetList.isEmpty()) {
            view_empty_state.visibility = View.VISIBLE
        } else {
            recycler_view.visibility = View.VISIBLE
            adapter.replaceData(tweetList)
        }
    }

    private fun shareTweet(item: PilpresTweet) {
        ShareUtil(context!!, item)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
    }

    override fun showLoading() {
        view_empty_state.visibility = View.GONE
        recycler_view.visibility = View.INVISIBLE
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        recycler_view.visibility = View.GONE
        progress_bar.visibility = View.GONE
    }

    override fun onDestroy() {
        (activity?.application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.RequestCode.BANNER_PILPRES -> hideBanner()
            }
        }
    }
}