package com.pantaubersama.app.ui.bidangkajiandialog

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.tags.TagItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_dialog_list.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class BidangKajianDialog : BaseDialogFragment<BidangKajianPresenter>(), BidangKajianView {

    @Inject
    override lateinit var presenter: BidangKajianPresenter

    lateinit var adapter: BidangKajianAdapter
    private var listener: DialogListener? = null

    private var keyword = ""

    override fun setLayout(): Int = R.layout.pilih_bidang_kajian

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
        setupSearchEdiText()
    }

    private fun getData() {
        adapter.setDataEnd(false)
        presenter.getTagList(1, keyword)
    }

    private fun setupRecycler() {
        adapter = BidangKajianAdapter()
        adapter.listener = object : BidangKajianAdapter.Listener {
            override fun onClick(item: TagItem) {
                listener?.onClickItem(item)
                dismiss()
            }
        }
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getTagList(it, keyword)
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            getData()
        }
    }

    private fun setupSearchEdiText() {
        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (textView.text.toString().isNotEmpty() && textView.text.toString().isNotBlank()) {
                    this.keyword = textView.text.toString()
                    getData()
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textView.windowToken, 0)
                    return@setOnEditorActionListener true
                }
            }
            false
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query?.isEmpty() == true) {
                    this@BidangKajianDialog.keyword = ""
                    getData()
                }
            }
        })
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false)
        if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
    }

    override fun onSuccessGetTags(tagList: MutableList<TagItem>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(tagList)
    }

    override fun onSuccessGetMoreTags(tagList: MutableList<TagItem>) {
        adapter.setLoaded()
        if (tagList.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(tagList)
    }

    override fun onEmptyTags() {
        tv_empty_state.text = getString(R.string.pencarian_tidak_ditemukan)
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun onErrorGetTags(t: Throwable) {
        view_fail_state.enableLottie(true, lottie_empty_state)
    }

    override fun onErrorGetMoreTags(t: Throwable) {
        adapter.setLoaded()
    }

    interface DialogListener {
        fun onClickItem(item: TagItem)
    }
}
