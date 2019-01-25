package com.pantaubersama.app.ui.search.person.filter

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_filter_orang.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterOrangActivity : BaseActivity<FilterOrangPresenter>(), FilterOrangView {
    private var searchOrangFilter: String? = null

    @Inject
    override lateinit var presenter: FilterOrangPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_orang
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Filter", R.color.white, 4f)
        presenter.loadUserFilter()
        btn_terapkan.setOnClickListener {
            applyFilter()
            searchOrangFilter?.let { it1 -> presenter.saveSearchOrangFilter(it1) }
        }
    }

    override fun onSuccessSaveFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showSuccessSaveFilterAlert() {
        ToastUtil.show(this@FilterOrangActivity, "Pengaturan tersimpan")
    }

    override fun showFailedSaveFilterAlert() {
        ToastUtil.show(this@FilterOrangActivity, "Gagal menyimpan pengaturan")
    }

    private fun applyFilter() {
        val userFilterSelectedId = user_filter.checkedRadioButtonId
        val userFilterSelectedValue = findViewById<RadioButton>(userFilterSelectedId).text
        when (userFilterSelectedValue) {
            getString(R.string.filter_all_label) -> searchOrangFilter = PantauConstants.FILTER_ORANG_ALL
            getString(R.string.filter_unverified_label) -> searchOrangFilter = PantauConstants.FILTER_ORANG_VERIFIED
            getString(R.string.filter_verified_label) -> searchOrangFilter = PantauConstants.FILTER_ORANG_UNVERIFIED
        }
    }

    override fun setFilter(searchOrangFilter: String) {
        when (searchOrangFilter) {
            PantauConstants.FILTER_ORANG_ALL -> filter_all.isChecked = true
            PantauConstants.FILTER_ORANG_VERIFIED -> filter_unverified.isChecked = true
            PantauConstants.FILTER_ORANG_UNVERIFIED -> filter_verified.isChecked = true
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_reset -> resetFilter()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetFilter() {
        presenter.saveSearchOrangFilter(PantauConstants.FILTER_ORANG_ALL)
    }
}
