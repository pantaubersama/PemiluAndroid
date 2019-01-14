package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.AnsweredQuestion
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.dip
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_kuis_summary.*
import javax.inject.Inject

class KuisSummaryActivity : BaseActivity<KuisSummaryPresenter>(), KuisSummaryView {

    @Inject
    override lateinit var presenter: KuisSummaryPresenter

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_kuis_summary

    private lateinit var kuisItem: KuisItem

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        KuisSummaryAdapter()
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        kuisItem = intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM) as KuisItem
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        iv_kuis_image.loadUrl(kuisItem.image.url)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(OffsetItemDecoration(dip(16)))

        close_button.setOnClickListener { onBackPressed() }

        presenter.getKuisSummary(kuisItem.id)
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showAnswers(list: List<AnsweredQuestion>) {
        adapter.items = list
    }

    companion object {
        fun setIntent(context: Context, kuisItem: KuisItem): Intent {
            val intent = Intent(context, KuisSummaryActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ITEM, kuisItem)
            return intent
        }
    }
}