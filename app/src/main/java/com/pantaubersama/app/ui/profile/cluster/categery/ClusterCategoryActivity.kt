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
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cluster_category.*
import kotlinx.android.synthetic.main.add_category_dialog.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import java.util.concurrent.TimeUnit
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
            adapter.setDataEnd(false)
            presenter.getCategories(page, perPage, query)
        }
        RxTextView.textChanges(cluster_category_search)
            .filter { it.isNotEmpty() }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { it.toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                this.query = it
                page = 1
                adapter.setDataEnd(false)
                presenter.getCategories(page, perPage, query)
            }
            .subscribe()
    }

    private fun setupCategories() {
        adapter = CategoriesAdapter()
        recycler_view.layoutManager =
            LinearLayoutManager(this@ClusterCategoryActivity, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        adapter.listener = object : CategoriesAdapter.Listener {
            override fun onClick(category: Category) {
                closeThisSection(category)
            }
        }
        adapter.addSupportLoadMore(recycler_view, perPage) {
            adapter.setLoading()
            presenter.getCategories(it, perPage, query)
        }
    }

    private fun closeThisSection(category: Category) {
        val intent = Intent()
        intent.putExtra(PantauConstants.Cluster.CATEGORY, category)
        setResult(Activity.RESULT_OK, intent)
        finish()
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
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showEmptyAlert() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(false)
        lottie_loading.enableLottie(false)
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

    override fun onSuccessAddNewCategory(category: Category) {
        newCategoryDialog.dismiss()
        closeThisSection(category)
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