package com.pantaubersama.app.ui.clusterdialog

import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.Gravity
import android.view.Window
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_dialog_cluster_list.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

/**
 * @author edityomurti on 26/12/2018 23:57
 */
class ClusterListDialog : BaseDialogFragment<ClusterListDialogPresenter>(), ClusterListDialogView {

    @Inject
    override lateinit var presenter: ClusterListDialogPresenter

    private var adapter: ClusterListDialogAdapter? = null
    private var listener: DialogListener? = null

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
        lottie_loading.visibility = View.GONE
        setupRecyclerView()
        presenter.getClusterList(1)
        setupSearchEditText()
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
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
    }

    private fun setupSearchEditText() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                adapter?.filter?.filter(s)
            }
        })
    }

    override fun showLoadingGetMoreClusters() {
        adapter?.setLoading()
    }

    override fun showClusters(clusterList: MutableList<ClusterItem>) {
        recycler_view.visibility = View.VISIBLE
        adapter?.setLoaded()
        adapter?.setDatas(clusterList as MutableList<ItemModel>)
    }

    override fun showMoreClusters(clusterList: MutableList<ClusterItem>) {
        adapter?.setLoaded()
        if (clusterList.size < presenter.perPage!!) {
            adapter?.setDataEnd(true)
        }
    }

    override fun showEmptyCluster() {
//        view_empty_state.visibleIf(true)
    }

    override fun showFailedGetClusters() {
//        view_fail_state.visibleIf(true)
    }

    override fun showFailedGetMoreClusters() {
        adapter?.setLoaded()
    }

    override fun setLayout(): Int {
        return R.layout.layout_dialog_cluster_list
    }

    override fun showLoading() {
        adapter?.setLoading()
//        view_empty_state.visibility = View.GONE
//        view_fail_state.visibility = View.GONE
        recycler_view.visibility = View.INVISIBLE
    }

    override fun dismissLoading() {
        adapter?.setLoaded()
        recycler_view.visibility = View.GONE
        lottie_loading.visibility = View.GONE
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    interface DialogListener {
        fun onClickItem(item: ClusterItem)
        fun onClickDefault()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    override fun onResume() {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }
}