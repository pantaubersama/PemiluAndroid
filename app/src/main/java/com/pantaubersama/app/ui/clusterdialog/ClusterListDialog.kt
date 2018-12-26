package com.pantaubersama.app.ui.clusterdialog

import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.viewholder.BaseDialogFragment
import com.pantaubersama.app.data.model.cluster.ClusterItem
import kotlinx.android.synthetic.main.layout_dialog_cluster_list.*
import timber.log.Timber

/**
 * @author edityomurti on 26/12/2018 23:57
 */
class ClusterListDialog : BaseDialogFragment<ClusterListDialogPresenter>(), ClusterListDialogView {
    private var adapter: ClusterListDialogAdapter? = null
    private var onClickListener: OnClickListener? = null

    companion object {
        private val TAG = ClusterListDialog::class.java.simpleName

        fun show(fragmentManager: FragmentManager, onClickListener: OnClickListener) {
            val dialog = ClusterListDialog()
            dialog.onClickListener = onClickListener
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun initPresenter(): ClusterListDialogPresenter? {
        return ClusterListDialogPresenter()
    }

    override fun initView(view: View) {
        setupRecyclerView()
        Timber.d("clusterList presenter = $presenter")
        presenter?.getClusterList()
        setupSearchEditText()
    }

    override fun showCluster(clusterList: MutableList<ClusterItem>) {
        Timber.d("clusterList size: %s", clusterList.size)
        recycler_view.visibility = View.VISIBLE
        adapter?.add(clusterList)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ClusterListDialogAdapter(context!!)
        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                onClickListener?.onClick(adapter?.get(position)!!)
                dismiss()
            }
        })
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
    }

    private fun setupSearchEditText() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                adapter?.filter?.filter(s)
            }

        })
    }


    override fun setLayout(): Int {
        return R.layout.layout_dialog_cluster_list
    }
    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface OnClickListener {
        fun onClick(item: ClusterItem)
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