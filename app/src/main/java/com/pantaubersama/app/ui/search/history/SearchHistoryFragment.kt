package com.pantaubersama.app.ui.search.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.search.SearchActivity
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_search_history.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class SearchHistoryFragment : BaseFragment<SearchHistoryPresenter>(), SearchHistoryView {

    @Inject override lateinit var presenter: SearchHistoryPresenter

    private lateinit var adapter: SearchHistoryAdapter

    override fun setLayout(): Int = R.layout.fragment_search_history

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        val TAG = SearchHistoryFragment::class.java.simpleName

        fun newInstance(): SearchHistoryFragment {
            val fragment = SearchHistoryFragment()
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        presenter.getSearchHistory()

        tv_clear_all.setOnClickListener { showAlertDialog(getString(R.string.txt_hapus_riwayat_pencarian), object : DialogListener {
            override fun onClickDelete() {
                clearAllSearchHistory()
            }
        }) }
    }

    private fun setupRecycler() {
        adapter = SearchHistoryAdapter(object : SearchHistoryAdapter.AdapterListener {
            override fun onClick(keyword: String) {
                (activity as SearchActivity).setKeyword(keyword)
            }

            override fun onLongClick(keyword: String, position: Int) {
                showAlertDialog(getString(R.string.txt_hapus_kata_kunci_ini).format(keyword), object : DialogListener {
                    override fun onClickDelete() {
                        clearKeyword(keyword)
                        adapter.deleteItem(position)
                    }
                })
            }
        })
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
    }

    override fun showSearchHistory(keywordList: MutableList<String>) {
        ll_search_history.visibleIf(true)
        adapter.data = keywordList
    }

    override fun onEmptySearchHistory() {
        tv_empty_search_history.visibleIf(true)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        ll_search_history.visibleIf(false)
        tv_empty_search_history.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    private fun clearAllSearchHistory() {
        presenter.clearSearchHistory()
    }

    private fun clearKeyword(keyword: String) {
        presenter.clearKeyword(keyword)
    }

    private fun showAlertDialog(message: String, listener: DialogListener) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton(getString(R.string.txt_hapus)) { dialog, _ ->
                listener.onClickDelete()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    interface DialogListener {
        fun onClickDelete()
    }
}
