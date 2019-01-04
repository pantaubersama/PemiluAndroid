package com.pantaubersama.app.ui.linimasa.janjipolitik

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapter
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_janji_politik.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import javax.inject.Inject

class JanjiPolitikFragment : BaseFragment<JanjiPolitikPresenter>(), JanjiPolitikView {
    val TAG = JanjiPolitikFragment::class.java.simpleName

    @Inject
    lateinit var janpolInteractor: JanjiPolitikInteractor

    @Inject
    lateinit var bannerInteractor: BannerInfoInteractor

    private lateinit var adapter: JanjiPolitikAdapter

    companion object {
        fun newInstance(): JanjiPolitikFragment {
            val fragment = JanjiPolitikFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initInjection() {
        (activity?.application as BaseApp).createActivityComponent(activity)?.inject(this)
    }

    override fun initPresenter(): JanjiPolitikPresenter? {
        return JanjiPolitikPresenter(janpolInteractor, bannerInteractor)
    }

    override fun initView(view: View) {
        setupRecyclerJanpol()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }

    override fun hideBanner() {
        layout_banner_janpol.visibility = View.GONE
    }

    private fun setupRecyclerJanpol() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = JanjiPolitikAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickJanPolContent(item: JanjiPolitik) {
                startActivity(DetailJanjiPolitikActivity.setIntent(context!!, item.id!!))
            }

            override fun onClickJanpolOption(item: JanjiPolitik) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickShare(item: JanjiPolitik) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        getJanjiPolitikList()
    }

    private fun getJanjiPolitikList() {
        presenter?.getList()
    }

    override fun showJanjiPolitikList(janjiPolitikList: MutableList<JanjiPolitik>) {
        recycler_view.visibility = View.VISIBLE
        if (adapter.itemCount != 0 && adapter.get<ItemModel>(0) is BannerInfo) {
            val bannerInfo = adapter.get<BannerInfo>(0)
            adapter.clear()
            adapter.addBanner(bannerInfo)
            adapter.addData(janjiPolitikList as MutableList<ItemModel>)
        } else {
            adapter.setDatas(janjiPolitikList as MutableList<ItemModel>)
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_janji_politik
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

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        (activity?.application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.RequestCode.BANNER_JANPOL -> hideBanner()
            }
        }
    }
}
