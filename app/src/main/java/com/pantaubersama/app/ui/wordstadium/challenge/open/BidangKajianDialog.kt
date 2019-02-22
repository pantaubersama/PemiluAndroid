package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.wordstadium.BidangKajian
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*

class BidangKajianDialog : BaseDialogFragment<BidangKajianPresenter>(), BidangKajianView {

    override var presenter: BidangKajianPresenter = BidangKajianPresenter()
    lateinit var adapter: BidangKajianAdapter
    private var listener: DialogListener? = null

    override fun setLayout(): Int {
        return R.layout.pilih_bidang_kajian
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        private val TAG = BidangKajianDialog::class.java.simpleName

        fun show(fragmentManager: FragmentManager, dialogListener: DialogListener) {
            val dialog = BidangKajianDialog()
            dialog.listener = dialogListener
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        getData()
    }

    private fun getData() {
        adapter.setDataEnd(false)
        swipe_refresh.isRefreshing = false
        adapter.clear()
        var bidangKajianList = ArrayList<BidangKajian>()
        val data1 = BidangKajian("01", "Ekonomi")
        bidangKajianList.add(data1)
        val data2 = BidangKajian("02", "Sosial")
        bidangKajianList.add(data2)
        val data3 = BidangKajian("03", "Budaya")
        bidangKajianList.add(data3)
        val data4 = BidangKajian("04", "Energi")
        bidangKajianList.add(data4)
        val data5 = BidangKajian("05", "Sumber Daya Alama")
        bidangKajianList.add(data5)
        adapter.addData(bidangKajianList)
    }

    private fun setupRecycler() {
        adapter = BidangKajianAdapter()
        adapter.listener = object : BidangKajianAdapter.Listener {
            override fun onClick(bidangKajian: BidangKajian) {
                listener?.onClickItem(bidangKajian)
                dismiss()
            }
        }
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            getData()
        }
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
        fun onClickItem(bidangKajian: BidangKajian)
    }
}
