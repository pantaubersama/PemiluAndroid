package com.pantaubersama.app.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.extrainteger.symbolic.ui.SymbolicLoginButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.linimasa.LinimasaFragment
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.note.CatatanPilihanActivity
import com.pantaubersama.app.ui.notification.NotifActivity
import com.pantaubersama.app.ui.penpol.PenPolFragment
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.search.SearchActivity
import com.pantaubersama.app.ui.merayakan.MerayakanFragment
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_OPEN_TAB_TYPE
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_KUIS
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_TANYA_KANDIDAT
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity<HomePresenter>(), HomeView {

    @Inject
    override lateinit var presenter: HomePresenter
    private var url: String? = null
    private var tabDest: Int? = null

    override fun statusBarColor(): Int = R.color.white
    override fun setLayout(): Int = R.layout.activity_home

    companion object {
        fun setIntentByOpenedTab(context: Context, tabDest: Int): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_OPEN_TAB_TYPE, tabDest)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(PantauConstants.URL)?.let { url = it }
        intent.getIntExtra(EXTRA_OPEN_TAB_TYPE, EXTRA_TYPE_TANYA_KANDIDAT).let { tabDest = it }
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

        btn_search.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        btn_pinned.setOnClickListener { startActivity(Intent(this, CatatanPilihanActivity::class.java)) }
        btn_notification.visibleIf(false) // di hide dulu, karena belum diimplementasi @edityo 6/2/19
        btn_notification.setOnClickListener { startActivity(Intent(this, NotifActivity::class.java)) }

        if (savedInstanceState == null) {
            when (tabDest) {
                EXTRA_TYPE_JANPOL -> {
                    showFragment(LinimasaFragment.newInstanceOpenJanpol(), LinimasaFragment.TAG)
                    navigation.selectedItemId = R.id.navigation_menyerap
                }
                EXTRA_TYPE_FEED -> {
                    showFragment(LinimasaFragment(), LinimasaFragment.TAG)
                    navigation.selectedItemId = R.id.navigation_menyerap
                }
                EXTRA_TYPE_KUIS -> {
                    showFragment(PenPolFragment.newInstanceOpenKuis(), PenPolFragment.TAG)
                    navigation.selectedItemId = R.id.navigation_menggali
                }
                else -> {
                    showFragment(PenPolFragment.newInstance(), PenPolFragment.TAG)
                    navigation.selectedItemId = R.id.navigation_menggali
                }
            }
        }

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val (fragment, tag) = when (item.itemId) {
                R.id.navigation_menyerap -> LinimasaFragment() to LinimasaFragment.TAG
                R.id.navigation_menggali -> PenPolFragment.newInstance() to PenPolFragment.TAG
//                R.id.navigation_menguji -> MengujiHomeFragment.newInstance() to MengujiHomeFragment.TAG
//                R.id.navigation_menjaga -> MenjagaFragment.newInstance() to MenjagaFragment.TAG   // di hide dulu, production belum ada api nya @edityo 30/01/19
                R.id.navigation_merayakan -> MerayakanFragment.newInstance() to MerayakanFragment.TAG
                else -> throw IllegalStateException("unknown menu")
            }
            showFragment(fragment, tag)
            true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                (supportFragmentManager.primaryNavigationFragment as HomeFragment).scrollToTop()
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {}
        })

        presenter.updateUser()
        url?.let { SymbolicLoginButton.loadPage(this@HomeActivity, it) }
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

    override fun onResume() {
        super.onResume()
        presenter.updateUser()
    }

    fun setupTabsAndFilter(viewPager: ViewPager, onFilterClicked: View.OnClickListener?) {
        tab_layout.setupWithViewPager(viewPager)
        btn_filter.setOnClickListener(onFilterClicked)
        btn_filter.visibleIf(onFilterClicked != null)
    }

    fun changeTopBarColor(appbarColor: Int, statusBarColor: Int) {
        val appbarDrawable = TransitionDrawable(arrayOf(appbar.background, ColorDrawable(appbarColor)))
        appbar.background = appbarDrawable.also { it.startTransition(150) }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = statusBarColor
        }
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
