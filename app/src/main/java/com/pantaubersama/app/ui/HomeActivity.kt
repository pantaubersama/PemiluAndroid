package com.pantaubersama.app.ui

import android.support.design.widget.BottomNavigationView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.home.HomePresenter
import com.pantaubersama.app.ui.home.HomeView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomePresenter>(), HomeView {
    override fun statusBarColor(): Int {
        return R.color.colorPrimaryDark
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun setupUI() {
        setSupportActionBar(toolbar_home)

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_linimasa -> {
                    message.setText(R.string.title_navbar_linimasa)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_penpol -> {
                    message.setText(R.string.title_navbar_penpol)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_wordstadium -> {
                    message.setText(R.string.title_navbar_wordstadium)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_lapor -> {
                    message.setText(R.string.title_navbar_lapor)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_rekap -> {
                    message.setText(R.string.title_navbar_rekap)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun setLayout(): Int {
        return R.layout.activity_home
    }

    override fun onSuccessLoadUser() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(message: String) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
