package com.pantaubersama.app.ui.note.partai

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment

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
