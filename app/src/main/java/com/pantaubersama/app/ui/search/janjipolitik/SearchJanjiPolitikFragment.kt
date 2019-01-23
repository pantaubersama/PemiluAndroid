package com.pantaubersama.app.ui.search.janjipolitik

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapter
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_FILTER_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_janji_politik.*
import kotlinx.android.synthetic.main.fragment_search_janji_politik.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchJanjiPolitikFragment : BaseFragment<SearchJanjiPolitikPresenter>(), SearchJanjiPolitikView, UpdateableFragment {
    private lateinit var keyword: String

    @Inject override lateinit var presenter: SearchJanjiPolitikPresenter
    private lateinit var adapter: JanjiPolitikAdapter

    override fun setLayout(): Int = R.layout.fragment_search_janji_politik

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        private val TAG = SearchJanjiPolitikFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchJanjiPolitikFragment {
            val fragment = SearchJanjiPolitikFragment()
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
        tv_filter.setOnClickListener { startActivityForResult(FilterJanjiPolitikActivity.setIntent(requireContext(), true), RC_FILTER_JANPOL) }
    }

    override fun getData(keyword: String) {
        this.keyword = keyword
        adapter.setDataEnd(false)
        presenter.getList(keyword, 1)
    }

    private fun setupRecycler() {
        val myProfile = presenter.getMyProfile()
        adapter = JanjiPolitikAdapter()

        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                // no banner in this section
            }

            override fun onClickJanPolContent(item: JanjiPolitik, position: Int) {
                startActivityForResult(DetailJanjiPolitikActivity.setIntent(requireContext(), item, position), RC_OPEN_DETAIL_JANPOL)
            }

            override fun onClickJanpolOption(item: JanjiPolitik, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_tanya_kandidat)
                if (myProfile.cluster != null &&
                    item.creator?.cluster != null &&
                    item.creator?.cluster?.id?.equals(myProfile.cluster?.id)!! &&
                    item.creator?.id.equals(myProfile.id) &&
                    myProfile.cluster?.isEligible!!) {
                } else {
                    dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
                }
                dialog.removeItem(R.id.report_tanya_kandidat_action)
                dialog.show()
                dialog.listener = object : OptionDialog.DialogListener {
                    override fun onClick(viewId: Int) {
                        when (viewId) {
                            R.id.copy_url_tanya_kandidat_action -> {
                                onClickCopyUrl(item.id)
                                dialog.dismiss()
                            }
                            R.id.share_tanya_kandidat_action -> {
                                onClickShare(item)
                                dialog.dismiss()
                            }
                            R.id.report_tanya_kandidat_action -> {
                                onClickLapor(item.id)
                                dialog.dismiss()
                            }
                            R.id.delete_tanya_kandidat_item_action -> {
                                val deleteDialog = DeleteConfimationDialog(
                                    requireContext(), getString(R.string.txt_delete_item_ini), position, item.id!!,
                                    listener = object : DeleteConfimationDialog.DialogListener {
                                        override fun onClickDeleteItem(id: String, position: Int) {
                                            showProgressDialog(getString(R.string.menghapus_janji_politik))
                                            presenter.deleteJanjiPolitik(id, position)
                                        }
                                    })
                                deleteDialog.show()
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onClickShare(item: JanjiPolitik) {
                ShareUtil.shareItem(requireContext(), item)
            }

            override fun onClickCopyUrl(id: String?) {
                id?.let { CopyUtil.copyJanpol(requireContext(), it) }
            }

            override fun onClickLapor(id: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getList(keyword, it)
        }

        swipe_refresh.setOnRefreshListener {
            getData(keyword)
            swipe_refresh.isRefreshing = false
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

    override fun showJanpolList(janjiPolitikList: MutableList<JanjiPolitik>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(janjiPolitikList)
    }

    override fun showMoreJanpolList(janjiPolitikList: MutableList<JanjiPolitik>) {
        adapter.setLoaded()
        if (janjiPolitikList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(janjiPolitikList)
    }

    override fun showEmptyData() {
//        view_empty_state.enableLottie(tr)
        view_empty_state.visibleIf(true)
    }

    override fun showFailedGetData() {
//        view_fail_state.enableLottie(true, lottie_fail_state)
        view_fail_state.visibleIf(true)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun onSuccessDeleteItem(position: Int) {
        dismissProgressDialog()
        adapter.deleteItem(position)
    }

    override fun onFailedDeleteItem(throwable: Throwable) {
        dismissProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_FILTER_JANPOL) getData(keyword)
        } else if (resultCode == PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL) {
            if (requestCode == RC_OPEN_DETAIL_JANPOL) {
                if (data != null && data.getIntExtra(EXTRA_ITEM_POSITION, -1) != -1) {
                    adapter.deleteItem(data.getIntExtra(EXTRA_ITEM_POSITION, -1))
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
