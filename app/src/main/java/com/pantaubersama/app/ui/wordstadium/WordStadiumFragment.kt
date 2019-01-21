package com.pantaubersama.app.ui.wordstadium

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants.Networking.URL_PANTAU_BERSAMA
import com.pantaubersama.app.utils.PantauConstants.Networking.URL_PANTAU_BERSAMA_FACEBOOK
import com.pantaubersama.app.utils.PantauConstants.Networking.URL_PANTAU_BERSAMA_INSTAGRAM
import com.pantaubersama.app.utils.PantauConstants.Networking.URL_PANTAU_BERSAMA_TWITTER
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.layout_coming_soon.*

class WordStadiumFragment : CommonFragment() {

    companion object {
        val TAG = WordStadiumFragment::class.java.simpleName

        fun newInstance(): WordStadiumFragment {
            return WordStadiumFragment()
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_word_stadium
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        lottie_coming_soon.enableLottie(true)

        iv_logo_pantau_web.setOnClickListener { ChromeTabUtil(context!!).loadUrl(URL_PANTAU_BERSAMA) }
        iv_logo_facebook.setOnClickListener { ChromeTabUtil(context!!).forceLoadUrl(URL_PANTAU_BERSAMA_FACEBOOK) }
        iv_logo_instagram.setOnClickListener { ChromeTabUtil(context!!).forceLoadUrl(URL_PANTAU_BERSAMA_INSTAGRAM) }
        iv_logo_twitter.setOnClickListener { ChromeTabUtil(context!!).forceLoadUrl(URL_PANTAU_BERSAMA_TWITTER) }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        lottie_coming_soon.enableLottie(!hidden)
    }
}