package com.pantaubersama.app.ui.merayakan.perhitungan

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment

class PerhitunganFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_perhitungan
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): PerhitunganFragment {
            return PerhitunganFragment()
        }

        val TAG: String = PerhitunganFragment::class.java.simpleName
    }
}
