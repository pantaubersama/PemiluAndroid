package com.pantaubersama.app.ui.penpol.kuis.filter

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.KuisInteractor
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_filter_kuis.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterKuisActivity : BaseActivity<FilterKuisPresenter>(), FilterKuisView {
    @Inject
    lateinit var kuisInteractor: KuisInteractor
    private var kuisFilter: String? = null

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun initPresenter(): FilterKuisPresenter? {
        return FilterKuisPresenter(kuisInteractor)
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        presenter?.getKuisFilterData()
        btn_terapkan.setOnClickListener {
            applyFilter()
            presenter?.saveKuisFilter(kuisFilter)
        }
    }

    private fun applyFilter() {
        val kuisFilterSelectedId = radio_group_kuis.checkedRadioButtonId
        val kuisFilterSelectedValue = findViewById<RadioButton>(kuisFilterSelectedId).text

        when (kuisFilterSelectedValue) {
            getString(R.string.filter_all_label) -> kuisFilter = PantauConstants.Kuis.Filter.KUIS_ALL
            getString(R.string.txt_belum_diikuti) -> kuisFilter = PantauConstants.Kuis.Filter.BELUM_DIIKUTI
            getString(R.string.txt_belum_selesai) -> kuisFilter = PantauConstants.Kuis.Filter.BELUM_SELESAI
            getString(R.string.txt_selesai) -> kuisFilter = PantauConstants.Kuis.Filter.SELESAI
        }
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
                presenter?.saveKuisFilter(PantauConstants.Kuis.Filter.KUIS_ALL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setKuisFilter(kuisFilter: String?) {
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
