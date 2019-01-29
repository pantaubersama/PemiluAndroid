package com.pantaubersama.app.ui.menjaga.filter.partiesdialog

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_dialog_list.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class PartiesDialog : BaseDialogFragment<PartiesDialogPresenter>(), PartiesDialogView {
    @Inject
    override lateinit var presenter: PartiesDialogPresenter
    var listener: DialogListener? = null
    private lateinit var adapter: PartiesDialogAdapter

    override fun setLayout(): Int {
        return R.layout.layout_dialog_list
    }

    companion object {
        private val TAG = PartiesDialog::class.java.simpleName

        fun show(fragmentManager: FragmentManager, dialogListener: DialogListener) {
            val dialog = PartiesDialog()
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

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = PartiesDialogAdapter()
        adapter.listener = object : PartiesDialogAdapter.Listener {
            override fun onClickItem(party: PoliticalParty) {
                listener?.onClickItem(party)
                dismiss()
            }

            override fun onClickDefault() {
                listener?.onClickDefault()
                dismiss()
            }
        }
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            getData()
        }
    }

    private fun getData() {
        adapter.setDataEnd(false)
        presenter.getParties()
    }

    private fun setupSearchEditText() {
        et_search.visibility = View.GONE
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

    override fun showPartai(parties: List<PoliticalParty>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(parties)
    }

    interface DialogListener {
        fun onClickItem(item: PoliticalParty)
        fun onClickDefault()
    }
}