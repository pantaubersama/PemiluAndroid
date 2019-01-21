package com.pantaubersama.app.ui.onboarding

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.fragment_onboarding_child1.*

class OnboardingChild1Fragment : CommonFragment() {
    override fun setLayout(): Int {
        return R.layout.fragment_onboarding_child1
    }

    override fun initView(view: View) {
        onboarding_1_animation.enableLottie(true)
    }
}
