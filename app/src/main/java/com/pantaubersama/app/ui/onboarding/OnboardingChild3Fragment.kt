package com.pantaubersama.app.ui.onboarding

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.fragment_onboarding_child3.*

class OnboardingChild3Fragment : CommonFragment() {

    companion object {
        fun newInstance(): OnboardingChild3Fragment {
            val fragment = OnboardingChild3Fragment()
            return fragment
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_onboarding_child3
    }

    override fun initView(view: View) {
    }

    fun startAnimation(start: Boolean) {
        onboarding_3_animation.enableLottie(start, false)
    }
}
