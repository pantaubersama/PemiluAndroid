package com.pantaubersama.app.ui.search.cluster.filter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.categorydialog.CategoryListDialog
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_filter_cluster.*
import kotlinx.android.synthetic.main.item_category.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterClusterActivity : BaseActivity<FilterClusterPresenter>(), FilterClusterView {

    private var categoryFilter: Category? = null

    @Inject override lateinit var presenter: FilterClusterPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_filter_cluster

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter_cluster), R.color.white, 4f)
        presenter.getFilter()

        rl_cluster_category_container.setOnClickListener {
            CategoryListDialog.show(supportFragmentManager, object : CategoryListDialog.DialogListener {
                override fun onClickItem(item: Category) {
                    selectCategory(item)
                }

                override fun onClickDefault() {
                    selectCategory(null)
                }
            })
        }

        btn_terapkan.setOnClickListener {
            presenter.setFilter(categoryFilter)
        }
    }

    private fun selectCategory(item: Category?) {
        this.categoryFilter = item
        category_name.text = item?.name ?: "Semua Kategori"
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showSelectedFilter(categoryItem: Category?) {
        selectCategory(categoryItem)
    }

    override fun onSuccessSetFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_reset -> {
                selectCategory(null)
                ToastUtil.show(this, "Filter telah di reset")
                btn_terapkan.performClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
