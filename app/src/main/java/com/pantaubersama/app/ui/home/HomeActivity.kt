package com.pantaubersama.app.ui.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.home.linimasa.LinimasaFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomePresenter>(), HomeView {

    private val linimasaFragment = LinimasaFragment()
    private val otherFrag = Fragment() // dummy

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
                .commit()

        activeFragment = linimasaFragment

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_linimasa -> {
                    activeFragment = linimasaFragment
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_penpol -> {
                    activeFragment = otherFrag
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_wordstadium -> {
                    activeFragment = otherFrag
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_lapor -> {
                    activeFragment = otherFrag
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_rekap -> {
                    activeFragment = otherFrag
                    return@OnNavigationItemSelectedListener true
                }
            }

            showActiveFragment()

            return@OnNavigationItemSelectedListener false
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showActiveFragment() {
        supportFragmentManager.beginTransaction()
                .hide(linimasaFragment)
                .show(activeFragment)
                .commit()
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
