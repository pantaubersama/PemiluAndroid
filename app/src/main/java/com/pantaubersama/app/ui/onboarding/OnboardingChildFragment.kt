package com.pantaubersama.app.ui.onboarding

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_LOTTIE_AUTOPLAY
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_LOTTIE_FILE_NAME
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ONBOARDING_DESC
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ONBOARDING_TITLE
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.fragment_onboarding_child.*

class OnboardingChildFragment : CommonFragment() {

    private var lottieFile: String? = null
    private var onboardingTitle: String? = null
    private var onboardingDesc: String? = null
    private var isAutoPlay = false

    override fun setLayout(): Int = R.layout.fragment_onboarding_child

    companion object {
        val TAG = OnboardingChildFragment::class.java.simpleName
        fun newInstance(onboardingTitle: String, onboardingDesc: String, lottieFile: String, autoPlay: Boolean = false): OnboardingChildFragment {
            val fragment = OnboardingChildFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_LOTTIE_FILE_NAME, lottieFile)
            bundle.putString(EXTRA_ONBOARDING_TITLE, onboardingTitle)
            bundle.putString(EXTRA_ONBOARDING_DESC, onboardingDesc)
            bundle.putBoolean(EXTRA_LOTTIE_AUTOPLAY, autoPlay)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString(EXTRA_LOTTIE_FILE_NAME, null)?.let { lottieFile = it }
        args?.getString(EXTRA_ONBOARDING_TITLE, null)?.let { onboardingTitle = it }
        args?.getString(EXTRA_ONBOARDING_DESC, null)?.let { onboardingDesc = it }
        args?.getBoolean(EXTRA_LOTTIE_AUTOPLAY, false)?.let { isAutoPlay = it }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        lottieFile?.let { lottie_onboarding_content.setAnimation(it) }
        onboardingTitle?.let { tv_onboarding_title.text = it }
        onboardingDesc?.let { tv_onboarding_desc.text = it }

        if (isAutoPlay) startAnimation(true)
    }

    fun startAnimation(start: Boolean) {
        lottie_onboarding_content.enableLottie(start, false, false)
        if (!start) lottie_onboarding_content.progress = 0f
    }
}