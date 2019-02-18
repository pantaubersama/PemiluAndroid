package com.pantaubersama.app.ui.debat

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.MessageAdapter
import kotlinx.android.synthetic.main.activity_debat.*
import kotlinx.android.synthetic.main.layout_detail_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_status_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_debat.*
import timber.log.Timber
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
        setupAppBarBehaviour()

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

    @SuppressLint("SetTextI18n")
    private fun setupAppBarBehaviour() {
        var isMainToolbarShown = true
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            Timber.d("OffsetChanged - verticalOffset : $verticalOffset")
            Timber.d("OffsetChanged - collTool Height : ${collapsing_toolbar.height}")
            Timber.d("OffsetChanged - collTool minHeight : ${ViewCompat.getMinimumHeight(collapsing_toolbar)}")
            Timber.d("OffsetChanged - calc : ${collapsing_toolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsing_toolbar)}")
            if (verticalOffset < -30) {
                if (isMainToolbarShown) {
                    isMainToolbarShown = false
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellow))))
                    Timber.d("OffsetChangedTransition - startTransition")
                    toolbar_debat.background = toolbarBackground.also { it.startTransition(150) }

                    tv_toolbar_title.setText("${tv_sisa_waktu.text.toString().toLowerCase()} tersisa")
                    tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saldo_waktu_white_24, 0, 0, 0)
                }
            } else if (verticalOffset == 0){
                if (!isMainToolbarShown) {
                    isMainToolbarShown = true
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellowAlpha))))
                    Timber.d("OffsetChangedTransition - reverseTransition")
                    toolbar_debat.background = toolbarBackground.also {
                        it.isCrossFadeEnabled = true
                        it.reverseTransition(150)
                    }

                    tv_toolbar_title.setText("LIVE NOW")
                    tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_debat_live, 0, 0, 0)
                }

            }
        })
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
