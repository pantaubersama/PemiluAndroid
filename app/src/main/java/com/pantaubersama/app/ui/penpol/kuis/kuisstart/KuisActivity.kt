package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.Question
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_kuis.*
import javax.inject.Inject
import kotlin.random.Random

class KuisActivity : BaseActivity<KuisQuestionPresenter>(), KuisQuestionView {

    @Inject
    override lateinit var presenter: KuisQuestionPresenter

    private var kuisId: String = ""
    private var kuisTitle: String = ""

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        kuisId = intent.getStringExtra(PantauConstants.Kuis.KUIS_ID)
        kuisTitle = intent.getStringExtra(PantauConstants.Kuis.KUIS_TITLE)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        back_icon.setOnClickListener {
            onBackPressed()
        }
        with(resources.obtainTypedArray(R.array.bg_kuis)) {
            val bgResource = getResourceId(Random.nextInt(1), R.drawable.kuis_background_1)
            iv_background.setImageResource(bgResource)
            recycle()
        }

        presenter.getQuestions(kuisId)
    }

    override fun setLayout(): Int {
        return R.layout.activity_kuis
    }

    override fun showLoading() {
        tv_question.visibleIf(false)
        progress_bar.visibleIf(true)
        answer_a_button.isEnabled = false
        answer_b_button.isEnabled = false
    }

    override fun dismissLoading() {
        tv_question.visibleIf(true)
        progress_bar.visibleIf(false)
        answer_a_button.isEnabled = true
        answer_b_button.isEnabled = true
    }

    override fun showQuestion(question: Question, questionNo: Int, total: Int) {
        val answerA = question.answers.firstOrNull() ?: return
        val answerB = question.answers.getOrNull(1) ?: return

        number_of_question_info.text = getString(R.string.question_number_info, questionNo, total)
        tv_question.text = question.content
        answer_a.text = answerA.content
        answer_b.text = answerB.content
        answer_a_button.setOnClickListener {
            presenter.answerQuestion(kuisId, question.id, answerA.id)
        }
        answer_b_button.setOnClickListener {
            presenter.answerQuestion(kuisId, question.id, answerB.id)
        }
    }

    override fun onKuisFinished() {
        val intent = KuisResultActivity.setIntent(this, kuisId, kuisTitle, true).apply {
            addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(PantauConstants.Kuis.KUIS_REFRESH, false)) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    companion object {
        fun setIntent(context: Context, kuisId: String, kuisTitle: String,
                      refreshOnReturn: Boolean = false): Intent {
            val intent = Intent(context, KuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ID, kuisId)
            intent.putExtra(PantauConstants.Kuis.KUIS_TITLE, kuisTitle)
            intent.putExtra(PantauConstants.Kuis.KUIS_REFRESH, refreshOnReturn)
            return intent
        }
    }
}