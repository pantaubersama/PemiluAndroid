package com.pantaubersama.app.ui.penpol.kuis.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.TeamPercentage
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_result.*
import javax.inject.Inject

class KuisResultActivity : BaseActivity<KuisResultPresenter>(), KuisResultView {

    @Inject
    override lateinit var presenter: KuisResultPresenter

    private lateinit var kuisItem: KuisItem

    override fun statusBarColor(): Int? = R.color.white

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        kuisItem = intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM) as KuisItem
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        btn_see_answers.setOnClickListener {
            startActivity(KuisSummaryActivity.setIntent(this, kuisItem))
        }

        presenter.getKuisResult(kuisItem.id)
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showResult(team: TeamPercentage, userName: String) {
        tv_kuis_result.text = spannable {
            +"Dari hasil pilhan Quiz ${kuisItem.title},\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${team.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(team.team.avatar)
        tv_percentage.text = "%.2f%%".format(team.percentage)
        tv_paslon_name.text = team.team.title
        btn_share.setOnClickListener {
            shareKuis(team.team.id)
        }
    }

    override fun setLayout(): Int = R.layout.activity_kuis_result

    override fun onBackPressed() {
        if (intent.getBooleanExtra(PantauConstants.Kuis.KUIS_REFRESH, false)) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    companion object {
        fun setIntent(context: Context, kuisItem: KuisItem, refreshOnReturn: Boolean = false): Intent {
            val intent = Intent(context, KuisResultActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ITEM, kuisItem)
            intent.putExtra(PantauConstants.Kuis.KUIS_REFRESH, refreshOnReturn)
            return intent
        }
    }

    private fun shareKuis(id: Int) {
        val targetedShareIntents: MutableList<Intent> = ArrayList()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = this?.packageManager?.queryIntentActivities(shareIntent, 0)
        if (!resInfo!!.isEmpty()) {
            for (resolveInfo in resInfo) {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")
                sendIntent.putExtra(Intent.EXTRA_TEXT, "pantau.co.id" + "/share/kuis/" + id)
                sendIntent.type = "text/plain"
                if (!resolveInfo.activityInfo.packageName.contains("pantaubersama")) {
                    sendIntent.`package` = resolveInfo.activityInfo.packageName
                    targetedShareIntents.add(sendIntent)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Bagikan dengan")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())
            startActivity(chooserIntent)
        }
    }
}