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
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_delete_confirmation_dialog.*
import javax.inject.Inject

class TanyaKandidatFragment : BaseFragment<TanyaKandidatPresenter>(), TanyaKandidatView {
    @Inject
    lateinit var tanyaInteractor: TanyaKandidatInteractor

    @Inject
    lateinit var dataCache: DataCache

    @Inject
    lateinit var bannerInfoInteractor: BannerInfoInteractor

    private var page = 1
    private var perPage = 10

    private var adapter: TanyaKandidatAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var isDataEnd = false
    private var isLoading = false

    override fun initInjection() {
        (activity?.application as BaseApp).createActivityComponent(activity)?.inject(this)
    }

    override fun initPresenter(): TanyaKandidatPresenter? {
        return TanyaKandidatPresenter(tanyaInteractor, bannerInfoInteractor)
    }

    override fun initView(view: View) {
        setupTanyaKandidatList()
        getDataList()
    }

    fun getDataList() {
        adapter?.setDataEnd(false)
        presenter?.getList()
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter?.addBanner(bannerInfo)
        refreshItem()
    }

    override fun hideBanner() {
        layout_banner_tanya_kandidat.visibility = View.GONE
    }

    private fun setupTanyaKandidatList() {
        val userId = dataCache.loadUserProfile().id
        adapter = TanyaKandidatAdapter()
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter?.addSupportLoadMore(recycler_view, object : BaseRecyclerAdapter.OnLoadMoreListener {
            override fun loadMore(page: Int) {
                adapter?.setLoading()
                presenter?.getTanyaKandidatList(page, perPage)
            }
        }, 5)

        adapter?.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivityForResult(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_PILPRES, bannerInfo), PantauConstants.RequestCode.BANNER_TANYA_KANDIDAT)
            }

            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
                val dialog = OptionDialog(context!!, item, R.layout.layout_option_dialog_tanya_kandidat)
                if (!item.user?.id.equals(userId)) {
                    dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
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

            override fun onClickUpvote(id: String?, isLiked: Boolean?, position: Int?) {
                presenter?.upVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME, isLiked, position)
            }

            override fun onClickDeleteItem(id: String?, position: Int?) {
                presenter?.deleteItem(id, position)
            }

            override fun onClickCopyUrl(id: String?) {
                val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val copyUri = Uri.parse("pantau.co.id/" + "share/tk/" + id)
                val clip = ClipData.newUri(activity?.contentResolver, "URI", copyUri)
                clipboard.primaryClip = clip
                ToastUtil.show(context!!, "Copied to clipboard")
            }

            override fun onClickLapor(id: String?) {
                presenter?.reportQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
            }
        }
        swipe_refresh.setOnRefreshListener {
            swipe_refresh?.isRefreshing = false
            getDataList()
        }
    }

    private fun refreshItem() {
        page = 1
        presenter?.getTanyaKandidatList(page, perPage)
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>) {
        recycler_view.visibility = View.VISIBLE
        if (adapter?.itemCount != 0 && adapter?.get<ItemModel>(0) is BannerInfo) {
            val bannerInfo = adapter?.get<BannerInfo>(0)
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
        view_empty_state.visibility = View.VISIBLE
    }

    override fun bindNextDataTanyaKandidat(questions: MutableList<Pertanyaan>) {
        adapter?.setLoaded()
        if (questions.size < perPage) {
            adapter?.setDataEnd(true)
        }
        adapter?.addData(questions as MutableList<ItemModel>)
    }

    override fun showEmptyNextDataAlert() {
        ToastUtil.show(context!!, "Gagal memuat lebih banyak pertanyaan")
    }

    override fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun setDataEnd(isDataEnd: Boolean) {
        this.isDataEnd = isDataEnd
    }

    override fun setLayout(): Int {
        return R.layout.fragment_tanya_kandidat
    }

    override fun showLoading() {
        view_empty_state.visibility = View.GONE
        recycler_view.visibility = View.INVISIBLE
        lottie_loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        lottie_loading.visibility = View.GONE
    }

    override fun showFailedGetDataAlert() {
        ToastUtil.show(context!!, "Gagal memuat daftar pertanyaan")
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

    override fun onDestroy() {
        (activity?.application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE -> {
                    adapter?.addItem((data?.getSerializableExtra(PantauConstants.TanyaKandidat.TANYA_KANDIDAT_DATA) as Pertanyaan), 0)
                    recycler_view.smoothScrollToPosition(0)
                }
                PantauConstants.RequestCode.BANNER_TANYA_KANDIDAT -> hideBanner()
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