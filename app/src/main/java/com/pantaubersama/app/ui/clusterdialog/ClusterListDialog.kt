package com.pantaubersama.app.ui.clusterdialog

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_dialog_list.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

/**
 * @author edityomurti on 26/12/2018 23:57
 */
class ClusterListDialog : BaseDialogFragment<ClusterListDialogPresenter>(), ClusterListDialogView {
    private var keyword: String = ""

    @Inject
    override lateinit var presenter: ClusterListDialogPresenter

    private var adapter: ClusterListDialogAdapter? = null
    private var listener: DialogListener? = null

    override fun setLayout(): Int = R.layout.layout_dialog_list

    companion object {
        private val TAG = ClusterListDialog::class.java.simpleName

        fun show(fragmentManager: FragmentManager, dialogListener: DialogListener) {
            val dialog = ClusterListDialog()
            dialog.listener = dialogListener
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupRecyclerView()
        getData()
        setupSearchEditText()
    }

    private fun getData() {
        adapter?.setDataEnd(false)
        presenter.getClusterList(keyword, 1)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ClusterListDialogAdapter()
        adapter?.listener = object : ClusterListDialogAdapter.AdapterListener {
            override fun onClickItem(item: ClusterItem) {
                listener?.onClickItem(item)
                dismiss()
            }

            override fun onClickDefault() {
                listener?.onClickDefault()
                dismiss()
            }
        }
        adapter?.addSupportLoadMore(recycler_view, 10) {
            adapter?.setLoading()
            presenter.getClusterList(keyword, it)
        }
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getData()
        }
    }

    private fun setupSearchEditText() {
        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (textView.text.toString().isNotEmpty() && textView.text.toString().isNotBlank()) {
                    this.keyword = textView.text.toString()
                    getData()
                    val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    override fun showClusters(clusterList: MutableList<ClusterItem>) {
        recycler_view.visibleIf(true)
        adapter?.setDatas(clusterList)
    }

    override fun showMoreClusters(clusterList: MutableList<ClusterItem>) {
        adapter?.setLoaded()
        if (clusterList.size < presenter.perPage) {
            adapter?.setDataEnd(true)
        }
        adapter?.addData(clusterList)
    }

    override fun showEmptyCluster() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun showFailedGetClusters() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showFailedGetMoreClusters() {
        adapter?.setLoaded()
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

    interface DialogListener {
        fun onClickItem(item: ClusterItem)
        fun onClickDefault()
    }
}