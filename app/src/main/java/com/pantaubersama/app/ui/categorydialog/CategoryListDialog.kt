package com.pantaubersama.app.ui.categorydialog

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.cluster.category.CategoriesAdapter
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

/**
 * @author edityomurti on 23/01/2019 10:50
 */
class CategoryListDialog : BaseDialogFragment<CategoryListDialogPresenter>(), CategoryListDialogView {
    private val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
    private var keyword: String = ""

    @Inject override lateinit var presenter: CategoryListDialogPresenter

    private lateinit var adapter: CategoriesAdapter
    private var listener: DialogListener? = null

    override fun setLayout(): Int = R.layout.layout_dialog_list

    companion object {
        private val TAG = CategoryListDialog::class.java.simpleName

        fun show(fragmentManager: FragmentManager, dialogListener: DialogListener) {
            val dialog = CategoryListDialog()
            dialog.listener = dialogListener
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupRecycler()
        getData()
        setupSearchEditText()
    }

    private fun setupSearchEditText() {
        et_search.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (textView.text.toString().isNotEmpty() && textView.text.toString().isNotBlank()) {
                this.keyword = textView.text.toString()
                getData()
                val imm = textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)
                return@setOnEditorActionListener true
//              }
            }
            false
        }
    }

    private fun setupRecycler() {
        adapter = CategoriesAdapter()
        adapter.listener = object : CategoriesAdapter.Listener {
            override fun onClick(category: Category) {
                if (category.id != DEFAULT_CATEGORY) listener?.onClickItem(category) else listener?.onClickDefault()
                dismiss()
            }
        }
        adapter.addSupportLoadMore(recycler_view, 10) {
            adapter.setLoading()
            presenter.getCategories(it, keyword)
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        adapter.setDataEnd(false)
        presenter.getCategories(1, keyword)
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
        fun onClickItem(item: Category)
        fun onClickDefault()
    }

    override fun showCategory(categories: MutableList<Category>) {
        swipe_refresh.isRefreshing = false
        recycler_view.visibleIf(true)
        if (categories.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.clear()
        adapter.addItem(Category(DEFAULT_CATEGORY, "Semua Kategori"))
        adapter.addData(categories)
    }

    override fun showMoreCategory(categories: MutableList<Category>) {
        adapter.setLoaded()
        if (categories.size < presenter.perPage) {
            adapter.setDataEnd(true)
        }
        adapter.addData(categories)
    }

    override fun onEmptyData() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun onFailedGetCategory() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun onFailedGetMoreCategory() {
        adapter.setLoaded()
    }
}