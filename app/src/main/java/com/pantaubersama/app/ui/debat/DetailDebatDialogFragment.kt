package com.pantaubersama.app.ui.debat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonDialogFragment

/**
 * @author edityomurti on 21/02/2019 13:26
 */
class DetailDebatDialogFragment : CommonDialogFragment() {
    override fun setLayout(): Int = R.layout.layout_detail_debat

    companion object {
        private val TAG = DetailDebatDialogFragment::class.java.simpleName

        fun show(fragmentManager: FragmentManager) {
            val dialog = DetailDebatDialogFragment()
            dialog.show(fragmentManager, TAG)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }
}