package com.pantaubersama.app.ui.penpol.kuis.filter

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_filter_kuis.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterKuisActivity : BaseActivity<FilterKuisPresenter>(), FilterKuisView {

    @Inject
    override lateinit var presenter: FilterKuisPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        presenter.getKuisFilterData()
        btn_terapkan.setOnClickListener {
            applyFilter()
        }
    }

    private fun applyFilter() {
        val filter = when (radio_group_kuis.checkedRadioButtonId) {
            R.id.radbtn_belum_diikuti -> PantauConstants.Kuis.Filter.BELUM_DIIKUTI
            R.id.radbtn_belum_selesai -> PantauConstants.Kuis.Filter.BELUM_SELESAI
            R.id.radbtn_selesai -> PantauConstants.Kuis.Filter.SELESAI
            else -> PantauConstants.Kuis.Filter.KUIS_ALL
        }

        presenter.saveKuisFilter(filter)
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_kuis
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                presenter.saveKuisFilter(PantauConstants.Kuis.Filter.KUIS_ALL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setKuisFilter(kuisFilter: String) {
        when (kuisFilter) {
            PantauConstants.Kuis.Filter.KUIS_ALL -> radbtn_semua.isChecked = true
            PantauConstants.Kuis.Filter.BELUM_DIIKUTI -> radbtn_belum_diikuti.isChecked = true
            PantauConstants.Kuis.Filter.BELUM_SELESAI -> radbtn_belum_selesai.isChecked = true
            PantauConstants.Kuis.Filter.SELESAI -> radbtn_selesai.isChecked = true
        }
    }

    override fun onSuccessSaveKuisFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showSuccessSaveKuisFilterAlert() {
        ToastUtil.show(this@FilterKuisActivity, "Pengaturan tersimpan")
    }

    override fun showFailedSaveKuisFilterAlert() {
        ToastUtil.show(this@FilterKuisActivity, "Gagal menyimpan pengaturan")
    }
}
