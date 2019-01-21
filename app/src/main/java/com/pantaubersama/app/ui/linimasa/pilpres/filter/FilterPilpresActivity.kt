package com.pantaubersama.app.ui.linimasa.pilpres.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IS_SEARCH_FILTER
import com.pantaubersama.app.utils.PantauConstants.Filter.Pilpres.FILTER_ALL
import com.pantaubersama.app.utils.PantauConstants.Filter.Pilpres.FILTER_TEAM_1
import com.pantaubersama.app.utils.PantauConstants.Filter.Pilpres.FILTER_TEAM_2
import kotlinx.android.synthetic.main.activity_filter_pilpres.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterPilpresActivity : BaseActivity<FilterPilpresPresenter>(), FilterPilpresView {

    private var isSearchFilter = false

    @Inject
    override lateinit var presenter: FilterPilpresPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    private var selectedFilter: String? = ""

    companion object {
        fun setIntent(context: Context, isSearchFilter: Boolean): Intent {
            val intent = Intent(context, FilterPilpresActivity::class.java)
            intent.putExtra(EXTRA_IS_SEARCH_FILTER, isSearchFilter)
            return intent
        }
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
        isSearchFilter = intent.getBooleanExtra(EXTRA_IS_SEARCH_FILTER, false)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)

        if (!isSearchFilter) {
            presenter.getFilter()
        } else {
            presenter.getSearchFilter()
        }

        radio_group_pilpres.setOnCheckedChangeListener { _, checkedId ->
            selectedFilter = when (checkedId) {
                R.id.radbtn_semua -> FILTER_ALL
                R.id.radbtn_capres1 -> FILTER_TEAM_1
                R.id.radbtn_capres2 -> FILTER_TEAM_2
                else -> FILTER_ALL
            }
        }

        btn_terapkan.setOnClickListener {
            if (!isSearchFilter) {
                selectedFilter?.let { presenter.setFilter(it) }
            } else {
                selectedFilter?.let { presenter.setSearchFilter(it) }
            }
        }
    }

    override fun showSelectedFilter(selectedFilter: String) {
        this.selectedFilter = selectedFilter
        radio_group_pilpres.check(when (selectedFilter) {
            FILTER_ALL -> R.id.radbtn_semua
            FILTER_TEAM_1 -> R.id.radbtn_capres1
            FILTER_TEAM_2 -> R.id.radbtn_capres2
            else -> R.id.radbtn_semua
        })
    }

    override fun onSuccessSetFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_pilpres
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
                radio_group_pilpres.check(R.id.radbtn_semua)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }
}