package com.pantaubersama.app.ui.onboarding

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.fragment_onboarding_child2.*

class OnboardingChild2Fragment : CommonFragment() {
    companion object {
        val TAG = OnboardingChild2Fragment::class.java.simpleName
    }

    override fun setLayout(): Int {
        return R.layout.fragment_onboarding_child2
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    fun startAnimation(start: Boolean) {
        onboarding_2_animation.enableLottie(start, false)
    }
}
