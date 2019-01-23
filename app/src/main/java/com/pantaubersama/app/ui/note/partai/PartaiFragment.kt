package com.pantaubersama.app.ui.note.partai

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.layout_coming_soon.*

class PartaiFragment : CommonFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
       // lanjut
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
