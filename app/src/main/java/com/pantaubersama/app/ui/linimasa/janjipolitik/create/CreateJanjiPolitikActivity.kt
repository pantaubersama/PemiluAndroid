package com.pantaubersama.app.ui.linimasa.janjipolitik.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import kotlinx.android.synthetic.main.activity_create_janji_politik.*
import kotlinx.android.synthetic.main.layout_editor_option.*

class CreateJanjiPolitikActivity : BaseActivity<CreateJanjiPolitikPresenter>(), CreateJanjiPolitikView {
    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun initPresenter(): CreateJanjiPolitikPresenter? {
        return CreateJanjiPolitikPresenter()
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_tab_janji_politik), R.color.white, 4f)
        et_create_janpol_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_current_length.text = query?.length.toString()
            }
        })
        setupEditor()
    }

    private fun setupEditor() {
        iv_editor_capital.setOnClickListener {}
        iv_editor_bold.setOnClickListener {}
        iv_editor_italic.setOnClickListener {}
        iv_editor_numbering.setOnClickListener {}
        iv_editor_bullet.setOnClickListener {}
        iv_editor_line.setOnClickListener {}
        iv_editor_image.setOnClickListener {}
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_janji_politik
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
