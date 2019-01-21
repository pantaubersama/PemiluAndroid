package com.pantaubersama.app.ui.onboarding

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.fragment_onboarding_child2.*

class OnboardingChild2Fragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_onboarding_child2
    }

    override fun initView(view: View) {
        onboarding_2_animation.enableLottie(true)
    }
}
