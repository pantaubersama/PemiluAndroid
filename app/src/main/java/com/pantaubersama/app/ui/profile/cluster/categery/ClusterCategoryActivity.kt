package com.pantaubersama.app.ui.profile.cluster.categery

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.*
import kotlinx.android.synthetic.main.activity_cluster_category.*
import kotlinx.android.synthetic.main.add_category_dialog.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import javax.inject.Inject

class ClusterCategoryActivity : BaseActivity<ClusterCategoryPresenter>(), ClusterCategoryView {
    @Inject
    override lateinit var presenter: ClusterCategoryPresenter
    private lateinit var newCategoryDialog: Dialog
    private lateinit var adapter: CategoriesAdapter
    private var page = 1
    private var perPage = 10
    private var query = ""

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupCategories()
        setupNewCategoryDialog()
        onclickAction()
        presenter.getCategories(page, perPage, query)
        swipe_refresh.setOnRefreshListener {
            page = 1
            presenter.getCategories(page, perPage, query)
        }
    }

    private fun setupCategories() {
        adapter = CategoriesAdapter()
        recycler_view.layoutManager =
            LinearLayoutManager(this@ClusterCategoryActivity, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        adapter.listener = object : CategoriesAdapter.Listener {
            override fun onClick(category: Category) {
                val intent = Intent()
                intent.putExtra(PantauConstants.Cluster.CATEGORY, category)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        adapter.addSupportLoadMore(recycler_view, object : BaseRecyclerAdapter.OnLoadMoreListener {
            override fun loadMore(page: Int) {
                adapter.setLoading()
                presenter.getCategories(page, perPage, query)
            }
        }, perPage)
    }

    private fun onclickAction() {
        cluster_category_back.setOnClickListener {
            finish()
        }
        cluster_category_add.setOnClickListener {
            newCategoryDialog.show()
        }
    }

    private fun setupNewCategoryDialog() {
        newCategoryDialog = Dialog(this@ClusterCategoryActivity)
        newCategoryDialog.setContentView(R.layout.add_category_dialog)
        newCategoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        newCategoryDialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                newCategoryDialog.dismiss()
                true
            } else {
                false
            }
        }
        newCategoryDialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = newCategoryDialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        newCategoryDialog.add_category_buat.setOnClickListener {
            presenter.createNewCategory(newCategoryDialog.add_category_text.text.toString())
            newCategoryDialog.dismiss()
        }
        newCategoryDialog.add_category_batal.setOnClickListener {
            newCategoryDialog.dismiss()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_cluster_category
    }

    override fun bindData(categories: MutableList<Category>) {
        swipe_refresh.isRefreshing = false
        recycler_view.visibleIf(true)
        adapter.setDatas(categories as MutableList<ItemModel>)
    }

    override fun showFailedGetCategoriesAlert() {
        view_fail_state.failStateVisible(true)
    }

    override fun showEmptyAlert() {
        view_empty_state.emptyStateVisible(true)
    }

    override fun showLoading() {
        lottie_loading.setVisible(true)
        view_empty_state.emptyStateVisible(false)
        view_fail_state.failStateVisible(false)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.setVisible(false)
    }

    override fun bindNextData(categories: MutableList<Category>) {
        adapter.setLoaded()
        adapter.addData(categories as MutableList<ItemModel>)
    }

    override fun setDataEnd() {
        adapter.setDataEnd(true)
    }

    override fun showAddCategoryLoading() {
        newCategoryDialog.loading.visibility = View.VISIBLE
    }

    override fun disableAddCategoryView() {
        newCategoryDialog.new_category_container.enable(false)
    }

    override fun onSuccessAddNewCategory() {
        newCategoryDialog.dismiss()
    }

    override fun enableAddCategoryView() {
        newCategoryDialog.new_category_container.enable(true)
    }

    override fun showFailedAddCategoryAlert() {
        ToastUtil.show(this@ClusterCategoryActivity, getString(R.string.failed_add_category_alert))
    }

    override fun dismissAddCategoryLoading() {
        newCategoryDialog.loading.visibility = View.GONE
    }
}