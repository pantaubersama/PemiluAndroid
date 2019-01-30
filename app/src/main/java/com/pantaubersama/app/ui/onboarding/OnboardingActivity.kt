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
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_1_DESC
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_1_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_1_TITLE
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_2_DESC
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_2_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_2_TITLE
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_3_DESC
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_3_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_3_TITLE
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_4_DESC
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_4_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_4_TITLE
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_5_DESC
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_5_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Onboarding.ONBOARDING_5_TITLE

class OnboardingActivity : BaseActivity<OnBoardingPresenter>() {

    private var onBoardingChildFragment: OnboardingChildFragment = OnboardingChildFragment.newInstance(
        ONBOARDING_1_TITLE, ONBOARDING_1_DESC, ONBOARDING_1_FILE_NAME, true)
    private var onBoardingChild2Fragment: OnboardingChildFragment = OnboardingChildFragment.newInstance(
        ONBOARDING_2_TITLE, ONBOARDING_2_DESC, ONBOARDING_2_FILE_NAME)
    private var onBoardingChild3Fragment: OnboardingChildFragment = OnboardingChildFragment.newInstance(
        ONBOARDING_3_TITLE, ONBOARDING_3_DESC, ONBOARDING_3_FILE_NAME)
    private var onBoardingChild4Fragment: OnboardingChildFragment = OnboardingChildFragment.newInstance(
        ONBOARDING_4_TITLE, ONBOARDING_4_DESC, ONBOARDING_4_FILE_NAME)
    private var onBoardingChild5Fragment: OnboardingChildFragment = OnboardingChildFragment.newInstance(
        ONBOARDING_5_TITLE, ONBOARDING_5_DESC, ONBOARDING_5_FILE_NAME)

    @Inject
    override lateinit var presenter: OnBoardingPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_onboarding

    override fun setupUI(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            supportFragmentManager.getFragment(savedInstanceState, "1")?.let { onBoardingChildFragment = it as OnboardingChildFragment }
            supportFragmentManager.getFragment(savedInstanceState, "2")?.let { onBoardingChild2Fragment = it as OnboardingChildFragment }
            supportFragmentManager.getFragment(savedInstanceState, "3")?.let { onBoardingChild3Fragment = it as OnboardingChildFragment }
            supportFragmentManager.getFragment(savedInstanceState, "4")?.let { onBoardingChild4Fragment = it as OnboardingChildFragment }
            supportFragmentManager.getFragment(savedInstanceState, "5")?.let { onBoardingChild5Fragment = it as OnboardingChildFragment }
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
                    0 -> onBoardingChildFragment
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
                        onBoardingChildFragment.startAnimation(true)
                        onBoardingChild2Fragment.startAnimation(false)
                    }
                    1 -> {
                        onBoardingChildFragment.startAnimation(false)
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
        if (onBoardingChildFragment.isAdded) supportFragmentManager.putFragment(outState, "1", onBoardingChildFragment)
        if (onBoardingChild2Fragment.isAdded) supportFragmentManager.putFragment(outState, "2", onBoardingChild2Fragment)
        if (onBoardingChild3Fragment.isAdded) supportFragmentManager.putFragment(outState, "3", onBoardingChild3Fragment)
        if (onBoardingChild4Fragment.isAdded) supportFragmentManager.putFragment(outState, "4", onBoardingChild4Fragment)
        if (onBoardingChild5Fragment.isAdded) supportFragmentManager.putFragment(outState, "5", onBoardingChild5Fragment)
    }
}
