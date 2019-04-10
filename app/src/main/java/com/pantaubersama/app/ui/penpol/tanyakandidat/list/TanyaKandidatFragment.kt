package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_QUESTION_ITEM
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_QUESTION
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_QUESTION
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_ITEM_CHANGED_QUESTION
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject
import kotlin.math.roundToInt
import com.pantaubersama.app.utils.* // ktlint-disable

class TanyaKandidatFragment : BaseFragment<TanyaKandidatPresenter>(), TanyaKandidatView {

    @Inject
    override lateinit var presenter: TanyaKandidatPresenter

    private var page = 1
    private var perPage = 10

    private var adapter: TanyaKandidatAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private lateinit var profile: Profile
    private var isNewInstance = true

    companion object {
        fun newInstance(): TanyaKandidatFragment {
            return TanyaKandidatFragment()
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        savedInstanceState?.getBoolean("is_new_instance")?.let { isNewInstance = it }
        presenter.getProfile()
        setupTanyaKandidatList()
        getDataList()
        recycler_view.setPadding(0, 0, 0,
            (resources.getDimension(R.dimen.fab_size) + resources.getDimension(R.dimen.fab_margin)).roundToInt())
    }

    override fun bindUserData(profile: Profile) {
        this.profile = profile
        fab_add.setOnClickListener {
            if (profile != EMPTY_PROFILE) {
                openCreatePertanyaan()
            } else {
                openLoginActivity()
            }
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun getDataList() {
        adapter?.setDataEnd(false)
        presenter.getBanner()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter?.addBanner(bannerInfo)
        refreshItem()
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter()
        adapter?.setHaveUser(profile)
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter?.addSupportLoadMore(recycler_view, 5) {
            adapter?.setLoading()
            presenter.getTanyaKandidatList(it, perPage)
        }

        adapter?.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(requireContext(), bannerInfo), PantauConstants.RequestCode.RC_BANNER_TANYA_KANDIDAT)
            }

            override fun onClickHeader() {
                if (profile != EMPTY_PROFILE) {
                    openCreatePertanyaan()
                } else {
                    openLoginActivity()
                }
            }

            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_common)
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
                                        override fun onClickDeleteItem(p0: String, p1: Int) {
                                            adapter?.listener?.onClickDeleteItem(item.id, position)
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
                startActivityForResult(intent, RC_OPEN_DETAIL_QUESTION)
            }

            override fun onclickActionUnauthorized() {
                openLoginActivity()
            }
        }
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> fab_add.show()
                    else -> fab_add.hide()
                }
            }
        })
        swipe_refresh.setOnRefreshListener {
            swipe_refresh?.isRefreshing = false
            getDataList()
        }
    }

    private fun openCreatePertanyaan() {
        val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
        startActivityForResult(intent, PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE)
    }

    private fun refreshItem() {
        page = 1
        adapter?.setDataEnd(false)
        presenter.getTanyaKandidatList(page, perPage)
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>) {
        recycler_view.visibleIf(true)
        if (adapter?.itemCount != 0 && adapter?.get(0) is BannerInfo) {
            val bannerInfo = adapter?.get(0) as BannerInfo
            adapter?.clear()
            adapter?.addBanner(bannerInfo)
            adapter?.addData(pertanyaanList as MutableList<ItemModel>)
            scrollToTop(false)
        } else {
            adapter?.setDatas(pertanyaanList as MutableList<ItemModel>)
        }
        adapter?.addHeader(profile)
    }

    override fun showEmptyDataAlert() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>) {
        adapter?.setLoaded()
        if (questions.size < perPage) {
            adapter?.setDataEnd(true)
        }
        adapter?.addData(questions as MutableList<ItemModel>)
    }

    override fun showEmptyNextDataAlert() {
//        ToastUtil.show(context!!, "Gagal memuat lebih banyak pertanyaan")
        adapter?.setLoaded()
        adapter?.setDataEnd(true)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_tanya_kandidat
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
        fab_add.hide()
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(false)
        fab_add.show()
    }

    override fun showFailedGetDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun onItemUpVoted(position: Int) {
        FacebookEventLogger.logRatedEvent(
            requireContext(),
            PantauConstants.TanyaKandidat.NAME,
            (adapter?.get(position) as Pertanyaan).body,
            (adapter?.get(position) as Pertanyaan).id,
            1,
            1.0)
    }

    override fun onFailedUpVoteItem(liked: Boolean, position: Int) {
        adapter?.reverseVote(liked, position)
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
        adapter?.deleteItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE -> {
                    adapter?.addItem((data?.getSerializableExtra(PantauConstants.TanyaKandidat.TANYA_KANDIDAT_DATA) as Pertanyaan), 2)
                    recycler_view.smoothScrollToPosition(0)
                }
                PantauConstants.TanyaKandidat.Filter.FILTER_TANYA_KANDIDAT_REQUEST_CODE -> {
                    refreshItem()
                }
            }
        } else if (requestCode == RC_OPEN_DETAIL_QUESTION) {
            if (resultCode == RESULT_DELETE_ITEM_QUESTION) {
                if (data != null && data.getIntExtra(EXTRA_ITEM_POSITION, -1) != -1) {
                    onItemDeleted(data.getIntExtra(EXTRA_ITEM_POSITION, -1))
                }
            } else if (resultCode == RESULT_ITEM_CHANGED_QUESTION) {
                if (data != null && data.getIntExtra(EXTRA_ITEM_POSITION, -1) != -1 && data.getSerializableExtra(EXTRA_QUESTION_ITEM) != null) {
                    val itemChangedPosition = data.getIntExtra(EXTRA_ITEM_POSITION, -1)
                    val itemChanged = data.getSerializableExtra(EXTRA_QUESTION_ITEM) as Pertanyaan
                    if (isNewInstance) {
                        adapter?.changeItem(itemChanged, itemChangedPosition)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_new_instance", false)
    }
}