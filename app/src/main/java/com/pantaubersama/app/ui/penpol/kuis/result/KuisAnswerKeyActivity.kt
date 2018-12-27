package com.pantaubersama.app.ui.penpol.kuis.result

import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.extensions.dip
import kotlinx.android.synthetic.main.activity_kuis_answer_key.*

class KuisAnswerKeyActivity : BaseActivity<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? = null

    override fun statusBarColor(): Int? = R.color.white

    override fun fetchIntentExtra() {
    }

    override fun setupUI() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = KuisAnswerKeyAdapter().apply { items = 10 }
        recycler_view.addItemDecoration(OffsetItemDecoration(dip(16)))

        close_button.setOnClickListener { onBackPressed() }
    }

    override fun setLayout(): Int = R.layout.activity_kuis_answer_key

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }
}