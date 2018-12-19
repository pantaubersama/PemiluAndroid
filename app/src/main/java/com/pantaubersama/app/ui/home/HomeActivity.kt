package com.pantaubersama.app.ui.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.home.linimasa.LinimasaFragment
import com.pantaubersama.app.ui.penpol.PenPolFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomePresenter>(), HomeView, BottomNavigationView.OnNavigationItemSelectedListener {

    private val linimasaFragment = LinimasaFragment()
    private val penPolFragment = PenPolFragment.newInstance()

    private lateinit var activeFragment: Fragment

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

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, linimasaFragment)
                .add(R.id.fragment_container, penPolFragment)
                .commit()

        activeFragment = linimasaFragment

        navigation.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_linimasa -> run {
                activeFragment = linimasaFragment
                return@run
            }
            R.id.navigation_penpol -> run {
                activeFragment = penPolFragment
                return@run
            }
            R.id.navigation_wordstadium -> run {
                activeFragment = penPolFragment
                return@run
            }
            R.id.navigation_lapor -> run {
                activeFragment = penPolFragment
                return@run
            }
            R.id.navigation_rekap -> run {
                activeFragment = penPolFragment
                return@run
            }
        }

        showActiveFragment()

        return true
    }

    private fun showActiveFragment() {
        supportFragmentManager.beginTransaction()
                .hide(linimasaFragment)
                .hide(penPolFragment)
                .show(activeFragment)
                .commit()
    }

    override fun onResume() {
        super.onResume()
        showActiveFragment()
    }

    override fun setLayout(): Int {
        return R.layout.activity_home
    }

    override fun onSuccessLoadUser() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
