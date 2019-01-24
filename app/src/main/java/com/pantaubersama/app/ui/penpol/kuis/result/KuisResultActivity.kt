package com.pantaubersama.app.ui.penpol.kuis.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisResult
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_QUIZ_PARTICIPATION_ID
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_result.*
import javax.inject.Inject
import kotlin.math.roundToInt

class KuisResultActivity : BaseActivity<KuisResultPresenter>(), KuisResultView {

    @Inject
    override lateinit var presenter: KuisResultPresenter

    private var kuisItem: KuisItem? = null
    private var quizParticipationId: String? = null

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_kuis_result

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM)?.let { kuisItem = it as KuisItem }
        intent.getStringExtra(EXTRA_QUIZ_PARTICIPATION_ID)?.let { quizParticipationId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        if (kuisItem != null) {
            kuisItem?.let { presenter.getKuisResult(it.id) }
        } else {
            quizParticipationId?.let { presenter.getKuisResultByQuizParticipationId(it) }
            btn_see_answers.visibleIf(false)
        }
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
        constraint_layout_content.visibleIf(false)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showResult(kuisResult: KuisResult, userName: String) {
        tv_kuis_result.text = spannable {
            +"Dari hasil pilhan di Quiz ${kuisResult.title},\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${kuisResult.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(kuisResult.team.avatar)
        tv_percentage.text = "%d%%".format(kuisResult.percentage.roundToInt())
        tv_paslon_name.text = kuisResult.team.title
        btn_share.setOnClickListener {
            ShareUtil.shareItem(this, kuisResult)
        }
        btn_see_answers.setOnClickListener {
            kuisItem?.let { kuisItem -> startActivity(KuisSummaryActivity.setIntent(this, kuisItem)) }
        }

        constraint_layout_content.visibleIf(true)
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(PantauConstants.Kuis.KUIS_REFRESH, false)) {
            setResult(Activity.RESULT_OK)
        } else if (isTaskRoot) {
            val intent = Intent(this@KuisResultActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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

        fun setIntent(context: Context, quizParticipationId: String): Intent {
            val intent = Intent(context, KuisResultActivity::class.java)
            intent.putExtra(EXTRA_QUIZ_PARTICIPATION_ID, quizParticipationId)
            return intent
        }
    }
}