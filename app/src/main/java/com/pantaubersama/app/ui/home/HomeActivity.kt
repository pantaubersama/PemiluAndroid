package com.pantaubersama.app.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.lapor.LaporFragment
import com.pantaubersama.app.ui.linimasa.LinimasaFragment
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.note.CatatanPilihanActivity
import com.pantaubersama.app.ui.penpol.PenPolFragment
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.quickcount.QuickCountFragment
import com.pantaubersama.app.ui.wordstadium.WordStadiumFragment
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity<HomePresenter>(), HomeView {

    @Inject
    override lateinit var presenter: HomePresenter

    override fun statusBarColor(): Int {
        return R.color.white
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar_home)
        iv_user_avatar.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        btn_pinned.setOnClickListener { startActivity(Intent(this, CatatanPilihanActivity::class.java)) }

        if (savedInstanceState == null) {
            showFragment(LinimasaFragment(), LinimasaFragment.TAG)
        }

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val (fragment, tag) = when (item.itemId) {
                R.id.navigation_menyerap -> LinimasaFragment() to LinimasaFragment.TAG
                R.id.navigation_menggali -> PenPolFragment.newInstance() to PenPolFragment.TAG
                R.id.navigation_menguji -> WordStadiumFragment.newInstance() to WordStadiumFragment.TAG
                R.id.navigation_menjaga -> LaporFragment.newInstance() to LaporFragment.TAG
                R.id.navigation_merayakan -> QuickCountFragment.newInstance() to QuickCountFragment.TAG
                else -> throw IllegalStateException("unknown menu")
            }
            showFragment(fragment, tag)
            true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        presenter.updateUser()
    }

    private fun showFragment(fragment: Fragment, tag: String) = with(supportFragmentManager) {
        val transaction = beginTransaction()
        var nextFragment = findFragmentByTag(tag)

        primaryNavigationFragment?.let {
            transaction.hide(it)
        }
        if (nextFragment != null) {
            transaction.show(nextFragment)
        } else {
            nextFragment = fragment
            transaction.add(R.id.fragment_container, nextFragment, tag)
        }

        transaction.setPrimaryNavigationFragment(nextFragment)
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }

    override fun setLayout(): Int {
        return R.layout.activity_home
    }

    override fun onResume() {
        super.onResume()
        presenter.updateUser()
    }

    override fun onSuccessLoadUser(profile: Profile) {
        iv_user_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
        if (profile != EMPTY_PROFILE) {
            iv_user_avatar.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            btn_pinned.setOnClickListener { startActivity(Intent(this, CatatanPilihanActivity::class.java)) }
        } else {
            iv_user_avatar.setOnClickListener {
                openLoginActivity()
            }

            btn_pinned.setOnClickListener { openLoginActivity() }
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
