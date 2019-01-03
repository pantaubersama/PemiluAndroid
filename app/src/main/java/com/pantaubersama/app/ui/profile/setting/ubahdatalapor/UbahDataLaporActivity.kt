package com.pantaubersama.app.ui.profile.setting.ubahdatalapor

import android.text.method.KeyListener
import android.widget.EditText
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ubah_data_lapor.*

class UbahDataLaporActivity : BaseActivity<UbahDataLaporPresenter>(), UbahDataLaporView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): UbahDataLaporPresenter? {
        return UbahDataLaporPresenter()
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_ubah_data_lapor), R.color.white, 4f)
        setEditextDisable(ubah_data_lapor_no_ktp)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_ubah_data_lapor
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }

    private fun onClickAction() {
        ubah_data_lapor_no_ktp.setOnClickListener {
            setEditextEnable(ubah_data_lapor_no_ktp)
        }
        ubah_data_lapor_submit.setOnClickListener {
            // submit data lapor
        }
    }

    fun setEditextDisable(editext: EditText) {
        editext.tag = editext.keyListener
        editext.keyListener = null
    }

    fun setEditextEnable(editext: EditText) {
        editext.setKeyListener(editext.getTag() as KeyListener)
    }
}
