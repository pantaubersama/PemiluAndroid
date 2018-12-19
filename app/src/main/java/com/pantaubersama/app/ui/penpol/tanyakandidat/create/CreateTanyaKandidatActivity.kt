package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import kotlinx.android.synthetic.main.activity_create_tanya_kandidat.*
import javax.inject.Inject

class CreateTanyaKandidatActivity : BaseActivity(), CreateTanyaKandidatView {
    @Inject
    lateinit var presenter: CreateTanyaKandidatPresenter

    override fun statusBarColor(): Int {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun setupUI() {
        presenter.attach(this)
        setupToolbar(true, getString(R.string.create_question), R.color.white, 4f)
        question?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
                question_length.text = string?.length.toString()
            }
        })
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_tanya_kandidat
    }

    override fun showLoading() {
        page_loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        page_loading.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> presenter.submitQuestion(question.text.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showEmptyQuestionAlert() {
        question.error = getString(R.string.empty_question_alert)
    }

    override fun onDestroy() {
        presenter.detach()
        (application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}
