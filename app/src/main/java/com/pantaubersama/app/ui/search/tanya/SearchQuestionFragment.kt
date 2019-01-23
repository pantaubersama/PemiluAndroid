package com.pantaubersama.app.ui.search.tanya

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatAdapter
import com.pantaubersama.app.ui.search.UpdateableFragment
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_SEARCH_KEYWORD
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_question.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchQuestionFragment : BaseFragment<SearchQuestionPresenter>(), UpdateableFragment, SearchQuestionView {
    private lateinit var keyword: String
    private lateinit var adapter: TanyaKandidatAdapter
    private lateinit var profile: Profile

    @Inject
    override lateinit var presenter: SearchQuestionPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int = R.layout.fragment_search_question

    companion object {
        private val TAG = SearchQuestionFragment::class.java.simpleName
        fun newInstance(keyword: String): SearchQuestionFragment {
            val fragment = SearchQuestionFragment()
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
        presenter.getProfile()
        setupTanyaKandidatList()
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
            val intent = Intent(requireContext(), FilterTanyaKandidatActivity::class.java)
            startActivityForResult(intent, PantauConstants.TanyaKandidat.Filter.FILTER_TANYA_KANDIDAT_REQUEST_CODE)
        }
    }

    override fun bindProfile(profile: Profile) {
        this.profile = profile
    }

    private fun setupTanyaKandidatList() {
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        adapter = TanyaKandidatAdapter()
        adapter.setHaveUser(profile)
        adapter.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(requireContext(), PantauConstants.Extra.EXTRA_TYPE_PILPRES, bannerInfo), PantauConstants.RequestCode.RC_BANNER_TANYA_KANDIDAT)
            }

            override fun onClickHeader() {
                val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
                startActivityForResult(intent, PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE)
            }

            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_tanya_kandidat)
                if (item.user.id.equals(profile.id)) {
                    dialog.removeItem(R.id.report_tanya_kandidat_action)
                } else {
                    dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
                }
                if (profile == EMPTY_PROFILE) {
                    dialog.removeItem(R.id.report_tanya_kandidat_action)
                }
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
                                    context!!, getString(R.string.txt_delete_item_ini),
                                    listener = object : DeleteConfimationDialog.DialogListener {
                                        override fun onClickDeleteItem(id: String, position: Int) {
                                            adapter.listener?.onClickDeleteItem(item.id, position)
                                        }
                                    })
                                deleteDialog.show()

                                dialog.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onClickShare(item: Pertanyaan?) {
                ShareUtil.shareItem(context!!, item)
            }

            override fun onClickUpvote(id: String, isLiked: Boolean, position: Int) {
                if (!isLiked) {
                    presenter.upVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME, isLiked, position)
                } else {
                    presenter.unVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME, isLiked, position)
                }
            }

            override fun onClickDeleteItem(id: String, position: Int) {
                showProgressDialog(getString(R.string.txt_delete_item_ini))
                presenter.deleteItem(id, position)
            }

            override fun onClickCopyUrl(id: String) {
                CopyUtil.copyTanyaKandidat(context!!, id)
            }

            override fun onClickLapor(id: String) {
                presenter.reportQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
            }

            override fun onClickContent(item: Pertanyaan, position: Int) {
                val intent = DetailTanyaKandidatActivity.setIntent(requireContext(), item, position)
                startActivityForResult(intent, PantauConstants.RequestCode.RC_OPEN_DETAIL_QUESTION)
            }

            override fun onclickActionUnauthorized() {
                openLoginActivity()
            }
        }
        recycler_view.adapter = adapter
        adapter.addSupportLoadMore(recycler_view, 5) {
            adapter.setLoading()
            presenter.searchQuestion(keyword, it, 10)
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun getData(keyword: String) {
        adapter.setDataEnd(false)
        presenter.searchQuestion(keyword, 1, 20)
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(pertanyaanList as MutableList<ItemModel>)
    }

    override fun showEmptyDataAlert() {
//        view_empty_state.enableLottie(true, lottie_empty_state)
        view_empty_state.visibleIf(true)
    }

    override fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>) {
        adapter.setLoaded()
        adapter.addData(questions as MutableList<ItemModel>)
    }

    override fun showEmptyNextDataAlert() {
//        ToastUtil.show(context!!, "Gagal memuat lebih banyak pertanyaan")
        adapter.setLoaded()
    }

    override fun setDataEnd() {
        adapter.setDataEnd(true)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(false)
    }

    override fun showFailedGetDataAlert() {
//        view_fail_state.enableLottie(true, lottie_fail_state)
        view_fail_state.visibleIf(true)
    }

    override fun onItemUpVoted() {
        // no need to do
    }

    override fun onFailedUpVoteItem(liked: Boolean, position: Int) {
        adapter.reverseVote(liked, position)
    }

    override fun showItemReportedAlert() {
        ToastUtil.show(context!!, getString(R.string.berhasil_melaporkan_pertanyaan))
    }

    override fun showFailedReportItem() {
        ToastUtil.show(context!!, getString(R.string.gagal_melaporkan_pertanyaan))
    }

    override fun showFailedDeleteItemAlert() {
        dismissProgressDialog()
        ToastUtil.show(context!!, getString(R.string.gagal_menghapus_pertanyaan))
    }

    override fun onItemDeleted(position: Int) {
        dismissProgressDialog()
        adapter.deleteItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.TanyaKandidat.Filter.FILTER_TANYA_KANDIDAT_REQUEST_CODE) {
                getData(keyword)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("keyword", keyword)
    }
}
