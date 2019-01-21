package com.pantaubersama.app.ui.note.partai

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.layout_coming_soon.*

class PartaiFragment : CommonFragment() {

    override fun initView(view: View) {
        iv_logo_pantau_web.setOnClickListener { ChromeTabUtil(requireContext()).loadUrl(PantauConstants.Networking.URL_PANTAU_BERSAMA) }
        iv_logo_facebook.setOnClickListener { ChromeTabUtil(requireContext()).forceLoadUrl(PantauConstants.Networking.URL_PANTAU_BERSAMA_FACEBOOK) }
        iv_logo_instagram.setOnClickListener { ChromeTabUtil(requireContext()).forceLoadUrl(PantauConstants.Networking.URL_PANTAU_BERSAMA_INSTAGRAM) }
        iv_logo_twitter.setOnClickListener { ChromeTabUtil(requireContext()).forceLoadUrl(PantauConstants.Networking.URL_PANTAU_BERSAMA_TWITTER) }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_partai
    }

    companion object {
        fun newInstance(): PartaiFragment {
            return PartaiFragment()
        }
    }
}
