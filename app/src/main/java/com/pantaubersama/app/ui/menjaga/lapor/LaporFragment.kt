package com.pantaubersama.app.ui.menjaga.lapor

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import kotlinx.android.synthetic.main.layout_common_recyclerview.*

class LaporFragment : CommonFragment() {

    companion object {
        fun newInstance(): LaporFragment {
            return LaporFragment()
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_lapor
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun scrollToTop(smoothScroll: Boolean) {
        if (smoothScroll) {
            recycler_view.smoothScrollToPosition(0)
        } else {
            recycler_view.scrollToPosition(0)
        }
    }
}
