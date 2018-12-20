package com.pantaubersama.app.ui.penpol.tanyakandidat.filter

import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter

class FilterTanyaKandidatActivity : BaseActivity<BasePresenter<*>>() {
    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun setupUI() {
        setupToolbar(true, "Filter", R.color.white, 4f)
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_tanya_kandidat
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> resetFilter()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetFilter() {
        // reset
    }
}
