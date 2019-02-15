package com.pantaubersama.app.ui.debat

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.MessageAdapter
import kotlinx.android.synthetic.main.activity_debat.*
import kotlinx.android.synthetic.main.layout_status_debat.*
import javax.inject.Inject

class DebatActivity : BaseActivity<DebatPresenter>(), DebatView {
    private lateinit var adapter: MessageAdapter

    @Inject
    override lateinit var presenter: DebatPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_debat

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
//        setupToolbar(true, "", android.R.color.transparent, 0f)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cl_btn_detail_debat.setOnClickListener {
            expandable_detail_debat.toggle(true)
            if (expandable_detail_debat.isExpanded) {
                iv_detail_debat_arrow.animate().rotation(180f).start()
            } else {
                iv_detail_debat_arrow.animate().rotation(0f).start()
            }
        }

        presenter.getMessage()
        setupList()
    }

    private fun setupList() {
        adapter = MessageAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMessage(messageList: MutableList<Message>) {
        adapter.setDatas(messageList)
    }

    override fun showLoading() {
        showProgressDialog("tunggu")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }
}
