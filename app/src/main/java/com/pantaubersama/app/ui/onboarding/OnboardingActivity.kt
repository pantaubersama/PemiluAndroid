package com.pantaubersama.app.ui.onboarding

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.TabView
import kotlinx.android.synthetic.main.activity_onboarding.*
import javax.inject.Inject
import android.view.animation.Animation.RELATIVE_TO_PARENT
import android.view.animation.TranslateAnimation
import androidx.viewpager.widget.ViewPager


class OnboardingActivity : BaseActivity<OnBoardingPresenter>() {
    private val onBoardingChild1Fragment: OnboardingChild1Fragment = OnboardingChild1Fragment()
    private val onBoardingChild2Fragment: OnboardingChild2Fragment = OnboardingChild2Fragment()

    @Inject
    override lateinit var presenter: OnBoardingPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_onboarding
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupViewPager()
    }

    private fun setupViewPager() {
        view_pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> onBoardingChild1Fragment
                    1 -> onBoardingChild2Fragment
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }
        dots_indicator.setViewPager(view_pager)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
