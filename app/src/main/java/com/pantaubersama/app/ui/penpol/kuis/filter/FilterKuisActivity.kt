package com.pantaubersama.app.ui.penpol.kuis.filter

import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter

class FilterKuisActivity : BaseActivity<BasePresenter<*>>() {
    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
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
        }
        return super.onOptionsItemSelected(item)
    }
}
