package com.pantaubersama.app.ui.profile.linimasa

import android.content.Intent
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
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_JANPOL
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_profile_janji_politik.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class ProfileJanjiPolitikFragment : BaseFragment<ProfileJanjiPolitikPresenter>(), ProfileJanjiPolitikView {

    @Inject
    override lateinit var presenter: ProfileJanjiPolitikPresenter

    private lateinit var adapter: JanjiPolitikAdapter

    companion object {
        val TAG = ProfileJanjiPolitikFragment::class.java.simpleName
        fun newInstance(): ProfileJanjiPolitikFragment {
            return ProfileJanjiPolitikFragment()
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int = R.layout.fragment_profile_janji_politik

    override fun initView(view: View) {
        setupRecyclerJanpol()
        getList()
    }

    private fun setupRecyclerJanpol() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = JanjiPolitikAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                // no banner in this list
            }

            override fun onClickJanPolContent(item: JanjiPolitik, position: Int) {
                startActivityForResult(DetailJanjiPolitikActivity.setIntent(requireContext(), item, position), RC_OPEN_DETAIL_JANPOL)
            }

            override fun onClickJanpolOption(item: JanjiPolitik, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_tanya_kandidat)
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
                                        override fun onClickDeleteItem(id: String, pos: Int) {
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
                CopyUtil.copyJanpol(requireContext(), id!!)
            }

            override fun onClickLapor(id: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getJanjiPolitikList(it)
        }
    }

    fun getList() {
        adapter.setDataEnd(false)
        presenter.getJanjiPolitikList(1)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun showJanpolList(janpolList: MutableList<JanjiPolitik>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(janpolList)
        recycler_view.swapAdapter(adapter, true)
    }

    override fun showMoreJanpolList(janpolList: MutableList<JanjiPolitik>) {
        adapter.setLoaded()
        if (janpolList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(janpolList)
        recycler_view.swapAdapter(adapter, true)
    }

    override fun showEmptyData() {
        view_empty_state.enableLottie(true, lottie_empty_state)
        tv_empty_state.text = "Hmm.. kamu belum pernah membuat postingan Janji Politik"
    }

    override fun showFailedGetData() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun onSuccessDeleteItem(position: Int) {
        dismissProgressDialog()
        adapter.deleteItem(position)
        if (adapter.itemCount == 0) {
            showLoading()
            dismissLoading()
            showEmptyData()
        }
    }

    override fun onFailedDeleteItem(throwable: Throwable) {
        dismissProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_DELETE_ITEM_JANPOL) {
            if (requestCode == RC_OPEN_DETAIL_JANPOL) {
                if (data != null && data.getIntExtra(EXTRA_ITEM_POSITION, -1) != -1) {
                    onSuccessDeleteItem(data.getIntExtra(EXTRA_ITEM_POSITION, -1))
                }
            }
        }
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
}