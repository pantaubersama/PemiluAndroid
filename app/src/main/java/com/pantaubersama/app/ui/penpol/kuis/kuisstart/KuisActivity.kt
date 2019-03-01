package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.Question
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.utils.HtmlTagHandler
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_kuis.*
import javax.inject.Inject
import kotlin.random.Random

class KuisActivity : BaseActivity<KuisQuestionPresenter>(), KuisQuestionView {

    @Inject
    override lateinit var presenter: KuisQuestionPresenter

    private lateinit var kuisItem: KuisItem

    override fun setLayout(): Int = R.layout.activity_kuis
    override fun statusBarColor(): Int? = 0

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        kuisItem = intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM) as KuisItem
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

        presenter.getQuestions(kuisItem.id)
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            answer_a.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerA.content), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
            answer_b.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerB.content), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
        } else {
            answer_a.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerA.content), null, HtmlTagHandler())
            answer_b.text = Html.fromHtml(HtmlTagHandler.customizeListTags(answerB.content), null, HtmlTagHandler())
        }
        answer_a_button.setOnClickListener {
            presenter.answerQuestion(kuisItem.id, question.id, answerA.id)
        }
        answer_b_button.setOnClickListener {
            presenter.answerQuestion(kuisItem.id, question.id, answerB.id)
        }
        scroll_view_question.scrollTo(0, 0)
        scroll_view.scrollTo(0, 0)
    }

    override fun onKuisFinished() {
        val intent = KuisResultActivity.setIntent(this, kuisItem, true).apply {
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
        fun setIntent(context: Context, kuisItem: KuisItem, refreshOnReturn: Boolean = false): Intent {
            val intent = Intent(context, KuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ITEM, kuisItem)
            intent.putExtra(PantauConstants.Kuis.KUIS_REFRESH, refreshOnReturn)
            return intent
        }
    }
}