package com.pantaubersama.app.ui.home

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.linimasa.LinimasaFragment
import com.pantaubersama.app.ui.penpol.PenPolFragment
import com.pantaubersama.app.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomePresenter>(), HomeView {

    private val linimasaFragment = LinimasaFragment()
    private val penPolFragment = PenPolFragment.newInstance()
    private val otherFrag = Fragment() // dummy

    private lateinit var activeFragment: Fragment

    override fun statusBarColor(): Int {
        return R.color.white
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun setupUI() {
        setSupportActionBar(toolbar_home)
        user_avatar.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
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
