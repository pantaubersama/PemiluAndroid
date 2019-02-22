package com.pantaubersama.app.ui.profile.penpol

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
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatAdapter
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.* // ktlint-disable
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_QUESTION
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_profile_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class ProfileTanyaKandidatFragment : BaseFragment<ProfileTanyaKandidatPresenter>(), ProfileTanyaKandidatView {

    @Inject override lateinit var presenter: ProfileTanyaKandidatPresenter

    private lateinit var adapter: TanyaKandidatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var profile: Profile
    private var userId: String? = null
    private var page: Int = 1
    private var perPage: Int = 20

    companion object {
        val TAG = ProfileTanyaKandidatFragment::class.java.simpleName

        fun newInstance(userId: String?): ProfileTanyaKandidatFragment {
            val fragment = ProfileTanyaKandidatFragment()
            val bundle = Bundle()
            bundle.putString(PantauConstants.Profile.USER_ID, userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int = R.layout.fragment_profile_tanya_kandidat

    override fun initView(view: View, savedInstanceState: Bundle?) {
        userId = arguments?.getString(PantauConstants.Profile.USER_ID)
        presenter.getProfile()
        setupTanyaKandidatList()
        getDataList()
    }

    override fun bindProfile(profile: Profile) {
        this.profile = profile
    }

    private fun getDataList() {
        adapter.setDataEnd(false)
        if (userId == null) {
            presenter.getTanyaKandidatList(1, perPage)
        } else {
            userId?.let { id -> presenter.getUserTanyaKandidat(1, id, perPage) }
        }
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter()
        adapter.setHaveUser(profile)
        adapter.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickHeader() {
                /* no header in this section */
            }

            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_common)
                dialog.removeItem(R.id.report_tanya_kandidat_action)
                if (userId != null) {
                    dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
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

            override fun onClickBanner(bannerInfo: BannerInfo) {
                // no banner in this section
            }

            override fun onClickShare(item: Pertanyaan?) {
                ShareUtil.shareItem(requireContext(), item)
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
                CopyUtil.copyTanyaKandidat(requireContext(), id)
            }

            override fun onClickLapor(id: String) {
                // lapor is hidden in this section
            }

            override fun onClickContent(item: Pertanyaan, position: Int) {
                val intent = DetailTanyaKandidatActivity.setIntent(requireContext(), item, position)
                startActivityForResult(intent, RC_OPEN_DETAIL_QUESTION)
            }

            override fun onclickActionUnauthorized() {
            }
        }
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view?.layoutManager = layoutManager
        recycler_view?.adapter = adapter
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            if (userId == null) {
                presenter.getTanyaKandidatList(it, perPage)
            } else {
                userId?.let { id -> presenter.getUserTanyaKandidat(it, id, perPage) }
            }
        }
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>) {
        recycler_view?.visibleIf(true)
        adapter.setDatas(pertanyaanList as MutableList<ItemModel>)
        if (pertanyaanList.size < perPage) {
            adapter.setDataEnd(true)
        }
        recycler_view.swapAdapter(adapter, true)
    }

    override fun showEmptyDataAlert() {
        view_empty_state.enableLottie(true, lottie_empty_state)
        tv_empty_state.text = "Belum pernah membuat pertanyaan"
    }

    override fun showFailedGetDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>) {
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        adapter.setLoaded()
        if (questions.size < perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(questions as MutableList<ItemModel>)
        recycler_view.swapAdapter(adapter, true)
    }

    override fun showEmptyNextDataAlert() {
        adapter.setLoaded()
        adapter.setDataEnd(true)
    }

    override fun onItemUpVoted(position: Int) {
        FacebookEventLogger.logRatedEvent(
            requireContext(),
            PantauConstants.TanyaKandidat.NAME,
            (adapter.get(position) as Pertanyaan).body,
            (adapter.get(position) as Pertanyaan).id,
            1,
            1.0)
    }

    override fun onFailedUpVoteItem(liked: Boolean, position: Int) {
        adapter.reverseVote(liked, position)
    }

    override fun showItemReportedAlert() {
        // nothing to do, no report in this section
    }

    override fun showFailedReportItem() {
        // nothing to do, no report in this section
    }

    override fun showFailedDeleteItemAlert() {
        dismissProgressDialog()
        ToastUtil.show(context!!, "Gagal menghapus pertanyaan")
    }

    override fun onItemDeleted(position: Int) {
        dismissProgressDialog()
        adapter.deleteItem(position)
        if (adapter.itemCount == 0) {
            showLoading()
            dismissLoading()
            showEmptyDataAlert()
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (view_empty_state.isVisible()) {
            lottie_empty_state.enableLottie(!hidden)
        }
        if (view_fail_state.isVisible()) {
            lottie_fail_state.enableLottie(!hidden)
        }
        if (lottie_loading.isVisible()) {
            lottie_loading.enableLottie(!hidden)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_OPEN_DETAIL_QUESTION) {
            if (resultCode == PantauConstants.ResultCode.RESULT_DELETE_ITEM_QUESTION) {
                if (data != null && data.getIntExtra(PantauConstants.Extra.EXTRA_ITEM_POSITION, -1) != -1) {
                    onItemDeleted(data.getIntExtra(PantauConstants.Extra.EXTRA_ITEM_POSITION, -1))
                }
            } else if (resultCode == PantauConstants.ResultCode.RESULT_ITEM_CHANGED_QUESTION) {
                if (data != null && data.getIntExtra(PantauConstants.Extra.EXTRA_ITEM_POSITION, -1) != -1 && data.getSerializableExtra(PantauConstants.Extra.EXTRA_QUESTION_ITEM) != null) {
                    val itemChangedPosition = data.getIntExtra(PantauConstants.Extra.EXTRA_ITEM_POSITION, -1)
                    val itemChanged = data.getSerializableExtra(PantauConstants.Extra.EXTRA_QUESTION_ITEM) as Pertanyaan
                    adapter.changeItem(itemChanged, itemChangedPosition)
                }
            }
        }
    }
}