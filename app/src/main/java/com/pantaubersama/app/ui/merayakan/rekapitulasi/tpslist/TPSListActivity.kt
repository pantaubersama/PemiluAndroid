package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

class TPSListActivity : BaseActivity<TPSListPresenter>(), TPSListView {

    @Inject
    override lateinit var presenter: TPSListPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_tpslist
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Sumberarum", R.color.white, 4f)
    }

    override fun showLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.close_action -> {
//                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, TPSListActivity::class.java))
        }
    }
}
