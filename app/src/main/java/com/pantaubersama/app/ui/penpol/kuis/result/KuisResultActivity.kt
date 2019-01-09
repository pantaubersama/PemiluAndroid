package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
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

    private var kuisId: String = ""
    private var kuisTitle: String = ""

    override fun statusBarColor(): Int? = R.color.white

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        kuisId = intent.getStringExtra(PantauConstants.Kuis.KUIS_ID)
        kuisTitle = intent.getStringExtra(PantauConstants.Kuis.KUIS_TITLE)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        btn_see_answers.setOnClickListener {
            startActivity(Intent(this, KuisAnswerKeyActivity::class.java))
        }

        presenter.getKuisResult(kuisId)
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showResult(team: TeamPercentage, userName: String) {
        tv_kuis_result.text = spannable {
            +"Dari hasil pilhan Quiz $kuisTitle,\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${team.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(team.team.avatar)
        tv_percentage.text = "%.2f%%".format(team.percentage)
        tv_paslon_name.text = team.team.title
    }

    override fun setLayout(): Int = R.layout.activity_kuis_result

    companion object {
        fun setIntent(context: Context, kuisId: String, kuisTitle: String): Intent {
            val intent = Intent(context, KuisResultActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ID, kuisId)
            intent.putExtra(PantauConstants.Kuis.KUIS_TITLE, kuisTitle)
            return intent
        }
    }
}