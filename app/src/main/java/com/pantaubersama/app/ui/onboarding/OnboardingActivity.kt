package com.pantaubersama.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.ui.login.LoginActivity

class OnboardingActivity : BaseActivity<OnBoardingPresenter>() {
    private var onBoardingChild1Fragment: OnboardingChild1Fragment = OnboardingChild1Fragment()
    private var onBoardingChild2Fragment: OnboardingChild2Fragment = OnboardingChild2Fragment()
    private var onBoardingChild3Fragment: OnboardingChild3Fragment = OnboardingChild3Fragment()
    private var onBoardingChild4Fragment: OnboardingChild4Fragment = OnboardingChild4Fragment()
    private var onBoardingChild5Fragment: OnboardingChild5Fragment = OnboardingChild5Fragment()

    @Inject
    override lateinit var presenter: OnBoardingPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_onboarding

    override fun setupUI(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            onBoardingChild1Fragment = supportFragmentManager.getFragment(savedInstanceState, OnboardingChild1Fragment.TAG) as OnboardingChild1Fragment
            onBoardingChild2Fragment = supportFragmentManager.getFragment(savedInstanceState, OnboardingChild2Fragment.TAG) as OnboardingChild2Fragment
            onBoardingChild3Fragment = supportFragmentManager.getFragment(savedInstanceState, OnboardingChild3Fragment.TAG) as OnboardingChild3Fragment
            onBoardingChild4Fragment = supportFragmentManager.getFragment(savedInstanceState, OnboardingChild4Fragment.TAG) as OnboardingChild4Fragment
            onBoardingChild5Fragment = supportFragmentManager.getFragment(savedInstanceState, OnboardingChild5Fragment.TAG) as OnboardingChild5Fragment
        }

        setupViewPager()

        skip_button.setOnClickListener {
            openLoginActivity()
        }
        next_button.setOnClickListener {
            if (view_pager.currentItem != 4) {
                view_pager.currentItem = view_pager.currentItem + 1
            } else {
                openLoginActivity()
            }
        }
    }

    private fun openLoginActivity() {
        presenter.setOnboardingComplete()
        val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun setupViewPager() {
        view_pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> onBoardingChild1Fragment
                    1 -> onBoardingChild2Fragment
                    2 -> onBoardingChild3Fragment
                    3 -> onBoardingChild4Fragment
                    4 -> onBoardingChild5Fragment
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return 5
            }
        }
        view_pager.offscreenPageLimit = 3
        dots_indicator.setViewPager(view_pager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        onBoardingChild1Fragment.startAnimation(true)
                        onBoardingChild2Fragment.startAnimation(false)
                    }
                    1 -> {
                        onBoardingChild1Fragment.startAnimation(false)
                        onBoardingChild2Fragment.startAnimation(true)
                        onBoardingChild3Fragment.startAnimation(false)
                    }
                    2 -> {
                        onBoardingChild2Fragment.startAnimation(false)
                        onBoardingChild3Fragment.startAnimation(true)
                        onBoardingChild4Fragment.startAnimation(false)
                    }
                    3 -> {
                        onBoardingChild3Fragment.startAnimation(false)
                        onBoardingChild4Fragment.startAnimation(true)
                        onBoardingChild5Fragment.startAnimation(false)
                    }
                    4 -> {
                        onBoardingChild4Fragment.startAnimation(false)
                        onBoardingChild5Fragment.startAnimation(true)
                    }
                }
                if (position == 4) {
                    next_button.text = getString(R.string.txt_selesai)
                } else {
                    next_button.text = getString(R.string.next_action)
                }
            }
        })
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        supportFragmentManager.putFragment(outState, OnboardingChild1Fragment.TAG, onBoardingChild1Fragment)
        supportFragmentManager.putFragment(outState, OnboardingChild2Fragment.TAG, onBoardingChild2Fragment)
        supportFragmentManager.putFragment(outState, OnboardingChild3Fragment.TAG, onBoardingChild3Fragment)
        supportFragmentManager.putFragment(outState, OnboardingChild4Fragment.TAG, onBoardingChild4Fragment)
        supportFragmentManager.putFragment(outState, OnboardingChild5Fragment.TAG, onBoardingChild5Fragment)
    }
}
