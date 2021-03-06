package com.pantaubersama.app.ui.penpol.tanyakandidat.filter

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_ALL
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_FALSE
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_TRUE
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_filter_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterTanyaKandidatActivity : BaseActivity<FilterTanyaKandidatPresenter>(), FilterTanyaKandidatView {

    @Inject
    override lateinit var presenter: FilterTanyaKandidatPresenter

    private var userFilter: String? = null
    private var orderFilter: String? = null
    private var isFromSearch = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        isFromSearch = intent.getBooleanExtra(PantauConstants.TanyaKandidat.Filter.IS_FROM_SEARCH, isFromSearch)
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Filter", R.color.white, 4f)
        if (!isFromSearch) {
            presenter.loadTanyaKandidatUserFilter()
            presenter.loadTanyaKandidatOrderFilter()
        } else {
            presenter.loadTanyaKandidatUserFilterSearch()
            presenter.loadTanyaKandidatOrderFilterSearch()
        }
        btn_terapkan.setOnClickListener {
            applyFilter()
            if (!isFromSearch) {
                presenter.saveTanyaKandidatFilter(userFilter, orderFilter)
            } else {
                presenter.saveTanyaKandidatFilterSaerch(userFilter, orderFilter)
            }
        }
    }

    private fun applyFilter() {
        val userFilterSelectedId = user_filter.checkedRadioButtonId
        val userFilterSelectedValue = findViewById<RadioButton>(userFilterSelectedId).text

        val orderFilterSelectedId = order_filter.checkedRadioButtonId
        val orderFilterSelectedValue = findViewById<RadioButton>(orderFilterSelectedId).text

        when (userFilterSelectedValue) {
            getString(R.string.filter_all_label) -> userFilter = USER_VERIFIED_ALL
            getString(R.string.filter_unverified_label) -> userFilter = USER_VERIFIED_FALSE
            getString(R.string.filter_verified_label) -> userFilter = USER_VERIFIED_TRUE
        }
        when (orderFilterSelectedValue) {
            getString(R.string.filter_trending_label) -> orderFilter = PantauConstants.TanyaKandidat.Filter.ByVotes.TRENDING
            getString(R.string.filter_latest_label) -> orderFilter = PantauConstants.TanyaKandidat.Filter.ByVotes.LATEST
            getString(R.string.filter_most_voted_label) -> orderFilter = PantauConstants.TanyaKandidat.Filter.ByVotes.MOST_VOTES
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_tanya_kandidat
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
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
        presenter.saveTanyaKandidatFilter(USER_VERIFIED_ALL, PantauConstants.TanyaKandidat.Filter.ByVotes.MOST_VOTES)
    }

    override fun onSuccessSaveFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showSuccessSaveFilterAlert() {
        ToastUtil.show(this@FilterTanyaKandidatActivity, "Pengaturan tersimpan")
    }

    override fun showFailedSaveFilterAlert() {
        ToastUtil.show(this@FilterTanyaKandidatActivity, "Gagal menyimpan pengaturan")
    }

    override fun setUserFilter(userfilter: String?) {
        when (userfilter) {
            USER_VERIFIED_ALL -> filter_all.isChecked = true
            USER_VERIFIED_FALSE -> filter_unverified.isChecked = true
            USER_VERIFIED_TRUE -> filter_verified.isChecked = true
        }
    }

    override fun showFailedLoadUserfilter() {
        ToastUtil.show(this@FilterTanyaKandidatActivity, "Gagal memuat filter berdasarkan pengguna")
    }

    override fun setOrderFilter(orderFilter: String?) {
        when (orderFilter) {
            PantauConstants.TanyaKandidat.Filter.ByVotes.TRENDING -> filter_trending.isChecked = true
            PantauConstants.TanyaKandidat.Filter.ByVotes.LATEST -> filter_latest.isChecked = true
            PantauConstants.TanyaKandidat.Filter.ByVotes.MOST_VOTES -> filter_most_voted.isChecked = true
        }
    }

    override fun showFailedLoadOrderfilter() {
        ToastUtil.show(this@FilterTanyaKandidatActivity, "Gagal memuat filter berdasarkan urutan")
    }
}
