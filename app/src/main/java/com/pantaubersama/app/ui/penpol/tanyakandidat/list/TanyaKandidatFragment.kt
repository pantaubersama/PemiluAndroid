package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_delete_confirmation_dialog.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject
import kotlin.math.roundToInt

class TanyaKandidatFragment : BaseFragment<TanyaKandidatPresenter>(), TanyaKandidatView {

    @Inject
    override lateinit var presenter: TanyaKandidatPresenter

    private var page = 1
    private var perPage = 10

    private var adapter: TanyaKandidatAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupTanyaKandidatList()
        getDataList()
        recycler_view.setPadding(0, 0, 0,
            (resources.getDimension(R.dimen.fab_size) + resources.getDimension(R.dimen.fab_margin)).roundToInt())
        fab_add.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivityForResult(intent, PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE)
        }
    }

    fun getDataList() {
        adapter?.setDataEnd(false)
        presenter.getList()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter?.addBanner(bannerInfo)
        refreshItem()
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter()
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter?.addSupportLoadMore(recycler_view, 5) {
            adapter?.setLoading()
            presenter.getTanyaKandidatList(it, perPage)
        }

        adapter?.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(requireContext(), PantauConstants.Extra.EXTRA_TYPE_PILPRES, bannerInfo), PantauConstants.RequestCode.RC_BANNER_TANYA_KANDIDAT)
            }

            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
                val dialog = OptionDialog(requireContext(), R.layout.layout_option_dialog_tanya_kandidat)
                if (item.user?.id.equals(presenter.getUserId())) {
                    dialog.removeItem(R.id.report_tanya_kandidat_action)
                } else {
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
                                showDeleteConfirmationDialog(item.id!!, position)
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onClickShare(item: Pertanyaan?) {
                ShareUtil.shareItem(context!!, item)
            }

            override fun onClickUpvote(id: String?, isLiked: Boolean, position: Int?) {
                if (!isLiked) {
                    presenter.upVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME, isLiked, position)
                } else {
                    presenter.unVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME, isLiked, position)
                }
            }

            override fun onClickDeleteItem(id: String?, position: Int?) {
                presenter.deleteItem(id, position)
            }

            override fun onClickCopyUrl(id: String?) {
                val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val copyUri = Uri.parse("pantau.co.id/" + "share/tk/" + id)
                val clip = ClipData.newUri(activity?.contentResolver, "URI", copyUri)
                clipboard.primaryClip = clip
                ToastUtil.show(context!!, "Copied to clipboard")
            }

            override fun onClickLapor(id: String?) {
                presenter.reportQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
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
            adapter?.addBanner(bannerInfo!!)
            adapter?.addData(pertanyaanList as MutableList<ItemModel>)
            scrollToTop(false)
        } else {
            adapter?.setDatas(pertanyaanList as MutableList<ItemModel>)
        }
        adapter?.addHeader()
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
        lottie_loading.enableLottie(true)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
        fab_add.hide()
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false)
        recycler_view.visibleIf(false)
        fab_add.show()
    }

    override fun showFailedGetDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun onItemUpVoted() {
        // no need to do
    }

    override fun onFailedUpVoteItem(liked: Boolean?, position: Int?) {
        adapter?.reverseVote(liked, position)
    }

    override fun showItemReportedAlert() {
        ToastUtil.show(context!!, "Berhasil melaporkan pertanyaan")
    }

    override fun showFailedReportItem() {
        ToastUtil.show(context!!, "Gagal melaporkan pertanyaan")
    }

    override fun showFailedDeleteItemAlert() {
        ToastUtil.show(context!!, "Gagal menghapus pertanyaan")
    }

    override fun onItemDeleted(position: Int?) {
        adapter?.deleteItem(position)
    }

    companion object {
        fun newInstance(): TanyaKandidatFragment {
            return TanyaKandidatFragment()
        }
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
        }
    }

    fun scrollToTop(smoothScroll: Boolean) {
        if (smoothScroll) {
            recycler_view.smoothScrollToPosition(0)
        } else {
            recycler_view.scrollToPosition(0)
        }
    }

    private fun showDeleteConfirmationDialog(id: String, position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_delete_confirmation_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else {
                false
            }
        }

        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        dialog.yes_button.setOnClickListener {
            adapter?.listener?.onClickDeleteItem(id, position)
            dialog.dismiss()
        }
        dialog.no_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}