package com.pantaubersama.app.ui.home.linimasa.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.FilterPilpresInteractor
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_filter_pilpres.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterPilpresActivity : BaseActivity<FilterPilpresPresenter>(), FilterPilpresView {
    @Inject
    lateinit var interactor: FilterPilpresInteractor

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun initPresenter(): FilterPilpresPresenter? {
        return FilterPilpresPresenter(interactor)
    }

    private var selectedFilter = 0

    fun setIntent(context: Context, selectedFilter: Int): Intent {
        val intent = Intent(context, FilterPilpresActivity::class.java)
//        intent.putExtra(PantauConstants.Extra.SELECTED_FILTER_PILPRES, selectedFilter)
        return intent
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        this.selectedFilter = intent.getIntExtra(PantauConstants.Extra.SELECTED_FILTER_PILPRES, 0)
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        presenter?.getFilter()

        radio_group_pilpres.setOnCheckedChangeListener { view, checkedId ->
            selectedFilter = when (checkedId) {
                R.id.radbtn_semua -> 0
                R.id.radbtn_capres1 -> 1
                R.id.radbtn_capres2 -> 2
                else -> 0
            }
        }

        btn_terapkan.setOnClickListener {
            presenter?.setFilter(selectedFilter)
        }
    }

    override fun showSelectedFilter(selectedFilter: Int) {
        radio_group_pilpres.check(when (selectedFilter) {
            0 -> R.id.radbtn_semua
            1 -> R.id.radbtn_capres1
            2 -> R.id.radbtn_capres2
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
                // reset
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        (application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}