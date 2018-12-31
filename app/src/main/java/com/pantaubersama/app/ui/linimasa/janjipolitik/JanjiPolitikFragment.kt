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
import com.pantaubersama.app.data.interactors.JanjiPolitikInteractor
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapter
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_janji_politik.*
import kotlinx.android.synthetic.main.layout_banner_container.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import javax.inject.Inject

class JanjiPolitikFragment : BaseFragment<JanjiPolitikPresenter>(), JanjiPolitikView {
    val TAG = JanjiPolitikFragment::class.java.simpleName

    @Inject
    lateinit var interactor: JanjiPolitikInteractor

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
        return JanjiPolitikPresenter(interactor)
    }

    override fun initView(view: View) {
        setupBanner()
        setupRecyclerJanpol()
    }

    private fun setupBanner() {
        presenter?.isBannerShown()
    }
    override fun showBanner() {
        layout_banner_janpol.visibility = View.VISIBLE
        tv_banner_text.text = getString(R.string.janpol_banner_text)
        iv_banner_image.setImageResource(R.drawable.ic_banner_janpol)
        fl_banner.setOnClickListener {
            startActivityForResult(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_JANPOL), PantauConstants.RequestCode.BANNER_JANPOL)
        }
        iv_banner_close.setOnClickListener {
            layout_banner_janpol.visibility = View.GONE
        }
    }

    override fun hideBanner() {
        layout_banner_janpol.visibility = View.GONE
    }

    private fun setupRecyclerJanpol() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = JanjiPolitikAdapter(context!!)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {
            override fun onClickContent(item: JanjiPolitik) {
                startActivity(DetailJanjiPolitikActivity.setIntent(context!!, item.id!!))
            }
        }
        getJanjiPolitikList()
    }

    private fun getJanjiPolitikList() {
        presenter?.getJanjiPolitikList()
    }

    override fun showJanjiPolitikList(janjiPolitikList: List<JanjiPolitik>) {
        if (janjiPolitikList.isEmpty()) {
            view_empty_state.visibility = View.VISIBLE
        } else {
            recycler_view.visibility = View.VISIBLE
            adapter.replaceData(janjiPolitikList)
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_janji_politik
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
