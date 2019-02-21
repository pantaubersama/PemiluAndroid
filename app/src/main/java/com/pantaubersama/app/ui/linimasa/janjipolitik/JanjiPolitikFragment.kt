package com.pantaubersama.app.ui.linimasa.janjipolitik

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
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapter
import com.pantaubersama.app.ui.linimasa.janjipolitik.create.CreateJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CREATE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_OPEN_DETAIL_JANPOL
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_janji_politik.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject
import kotlin.math.roundToInt

class JanjiPolitikFragment : BaseFragment<JanjiPolitikPresenter>(), JanjiPolitikView {
    val TAG = JanjiPolitikFragment::class.java.simpleName

    @Inject
    override lateinit var presenter: JanjiPolitikPresenter

    private lateinit var adapter: JanjiPolitikAdapter

    companion object {
        fun newInstance(): JanjiPolitikFragment {
            val fragment = JanjiPolitikFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        if (presenter.isUserEligible()) {
            fab_add.visibleIf(true)
            fab_add.setOnClickListener {
                val intent = Intent(context, CreateJanjiPolitikActivity::class.java)
                startActivityForResult(intent, RC_CREATE_JANPOL)
            }
        } else {
            fab_add.visibleIf(false)
        }
        setupRecyclerJanpol()
        getJanjiPolitikList()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }

    private fun setupRecyclerJanpol() {
        val myProfile = presenter.getMyProfile()
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = JanjiPolitikAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {

            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivity(BannerInfoActivity.setIntent(requireContext(), PantauConstants.Extra.EXTRA_TYPE_JANPOL, bannerInfo))
            }

            override fun onClickJanPolContent(item: JanjiPolitik, position: Int) {
                startActivityForResult(DetailJanjiPolitikActivity.setIntent(requireContext(), item, position), RC_OPEN_DETAIL_JANPOL)
            }

            override fun onClickJanpolOption(item: JanjiPolitik, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_common)
                if (myProfile.cluster != null &&
                    item.creator?.cluster != null &&
                    item.creator?.cluster?.id?.equals(myProfile.cluster?.id) == true &&
                    item.creator?.id.equals(myProfile.id) &&
                    myProfile.cluster?.isEligible!!) {
//                    dialog.removeItem(R.id.report_tanya_kandidat_action)
                } else {
                    dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
                }
                dialog.removeItem(R.id.report_tanya_kandidat_action) // dihilangkan sementara karena belum ada endpoint @edityo 08/01/19
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
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }
        }

        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getJanjiPolitikList(it)
        }

        if (presenter.isUserEligible()) {
            recycler_view.setPadding(0, 0, 0,
                (resources.getDimension(R.dimen.fab_size) + resources.getDimension(R.dimen.fab_margin)).roundToInt())

            recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> fab_add.show()
                        else -> fab_add.hide()
                    }
                }
            })
        }

        swipe_refresh.setOnRefreshListener {
            getJanjiPolitikList()
            swipe_refresh.isRefreshing = false
        }
    }

    fun getJanjiPolitikList() {
        adapter.setDataEnd(false)
        presenter.getList()
    }

    override fun showJanpolList(janjiPolitikList: MutableList<JanjiPolitik>) {
        recycler_view.visibleIf(true)
        if (adapter.itemCount != 0 && adapter.get(0) is BannerInfo) {
            val bannerInfo = adapter.get(0) as BannerInfo
            adapter.clear()
            adapter.addBanner(bannerInfo)
            adapter.addData(janjiPolitikList)
            scrollToTop(false)
        } else {
            adapter.setDatas(janjiPolitikList)
        }
    }

    override fun showMoreJanpolList(janjiPolitikList: MutableList<JanjiPolitik>) {
        adapter.setLoaded()
        if (janjiPolitikList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(janjiPolitikList)
    }

    override fun showEmptyData() {
        view_empty_state.enableLottie(true, lottie_empty_state)
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
    }

    override fun onFailedDeleteItem(throwable: Throwable) {
        dismissProgressDialog()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_janji_politik
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
        if (presenter.isUserEligible()) {
            fab_add.hide()
        }
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.enableLottie(false, lottie_loading)
        if (presenter.isUserEligible()) {
            fab_add.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_CREATE_JANPOL -> {
//                    if (adapter.get(0) is BannerInfo) {
//                        adapter.addItem(data?.getSerializableExtra(EXTRA_JANPOL_ITEM) as ItemModel, 1)
//                    } else {
//                        adapter.addItem(data?.getSerializableExtra(EXTRA_JANPOL_ITEM) as ItemModel, 0)
//                        scrollToTop(false)
//                    }
                    getJanjiPolitikList()
                }
            }
        } else if (resultCode == RESULT_DELETE_ITEM_JANPOL) {
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
