package com.pantaubersama.app.ui.home

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.home.linimasa.LinimasaFragment
import com.pantaubersama.app.ui.penpol.PenPolFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), HomeView {

    private val linimasaFragment = LinimasaFragment()
    private val penPolFragment = PenPolFragment.newInstance()
    private val otherFrag = Fragment() // dummy

    private lateinit var activeFragment: Fragment

    override fun statusBarColor(): Int {
        return R.color.colorPrimaryDark
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun initPresenter(): HomePresenter {
//        return HomePresenter()
//    }

    override fun setupUI() {
        setSupportActionBar(toolbar_home)

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, linimasaFragment)
                .add(R.id.fragment_container, penPolFragment)
                .commit()

        activeFragment = linimasaFragment

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                    activeFragment = otherFrag
                    return@run
                }
                R.id.navigation_lapor -> run {
                    activeFragment = otherFrag
                    return@run
                }
                R.id.navigation_rekap -> run {
                    activeFragment = otherFrag
                    return@run
                }
            }

            showActiveFragment()

            return@OnNavigationItemSelectedListener true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_linimasa
    }

    private fun showActiveFragment() {
        supportFragmentManager.beginTransaction()
                .hide(linimasaFragment)
                .hide(penPolFragment)
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
