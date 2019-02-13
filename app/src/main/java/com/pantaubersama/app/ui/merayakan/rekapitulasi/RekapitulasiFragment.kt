package com.pantaubersama.app.ui.merayakan.rekapitulasi

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment

class RekapitulasiFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_rekapitulasi
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): RekapitulasiFragment {
            return RekapitulasiFragment()
        }

        val TAG: String = RekapitulasiFragment::class.java.simpleName
    }
}
