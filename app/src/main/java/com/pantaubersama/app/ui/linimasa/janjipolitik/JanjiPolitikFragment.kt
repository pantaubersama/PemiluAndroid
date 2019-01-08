package com.pantaubersama.app.ui.linimasa.janjipolitik

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapter
import com.pantaubersama.app.ui.linimasa.janjipolitik.create.CreateJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CREATE_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.emptyStateVisible
import com.pantaubersama.app.utils.extensions.failStateVisible
import com.pantaubersama.app.utils.extensions.setVisible
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_janji_politik.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
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

    override fun initView(view: View) {
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
    }

    override fun showBanner(bannerInfo: BannerInfo) {
        adapter.addBanner(bannerInfo)
    }

    private fun setupRecyclerJanpol() {
        val userId = presenter.getUserId()
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = JanjiPolitikAdapter()
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.listener = object : JanjiPolitikAdapter.AdapterListener {

            override fun onClickBanner(bannerInfo: BannerInfo) {
                startActivity(BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_JANPOL, bannerInfo))
            }

            override fun onClickJanPolContent(item: JanjiPolitik) {
                startActivity(DetailJanjiPolitikActivity.setIntent(context!!, item.id!!))
            }

            override fun onClickJanpolOption(item: JanjiPolitik, position: Int) {
                val dialog = OptionDialog(context!!, R.layout.layout_option_dialog_tanya_kandidat)
                if (item.creator?.id.equals(userId)) {
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
                                val deleteDialog = DeleteConfimationDialog(context!!, getString(R.string.txt_delete_item_ini), position, item.id!!)
                                deleteDialog.show()
                                deleteDialog.listener = object : DeleteConfimationDialog.DialogListener {
                                    override fun onClickDeleteItem(id: String, position: Int) {
//                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    }
                                }
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onClickShare(item: JanjiPolitik) {
                ShareUtil.shareItem(context!!, item)
            }

            override fun onClickCopyUrl(id: String?) {
                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, "janpol id : $id")
                clipboard.primaryClip = clip
                ToastUtil.show(context!!, "janji politik telah disalin")
            }

            override fun onClickLapor(id: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        getJanjiPolitikList()
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
        adapter.addData(janjiPolitikList as MutableList<ItemModel>)
    }

    override fun showEmptyData() {
        view_empty_state.emptyStateVisible(true)
    }

    override fun showFailedGetData() {
        view_fail_state.failStateVisible(true)
    }

    override fun showFailedGetMoreData() {
        adapter.setLoaded()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_janji_politik
    }

    override fun showLoading() {
        lottie_loading.setVisible(true)
        view_empty_state.emptyStateVisible(false)
        view_fail_state.failStateVisible(false)
        recycler_view.visibleIf(false)
        if (presenter.isUserEligible()) {
            fab_add.hide()
        }
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.setVisible(false)
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
        }
    }

    fun scrollToTop(smoothScroll: Boolean) {
        if (smoothScroll) {
            recycler_view.smoothScrollToPosition(0)
        } else {
            recycler_view.scrollToPosition(0)
        }
    }
}
