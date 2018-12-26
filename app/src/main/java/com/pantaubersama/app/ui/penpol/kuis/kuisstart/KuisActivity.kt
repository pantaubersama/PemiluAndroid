package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import android.content.Context
import android.content.Intent
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_kuis.*

class KuisActivity : BaseActivity<BasePresenter<*>>() {

    private var questionNumber: Int = 0

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun fetchIntentExtra() {
        questionNumber = intent.getIntExtra(PantauConstants.Kuis.KUIS_NUMBER, 0)
    }

    override fun setupUI() {
        back_icon.setOnClickListener {
            finish()
        }

        number_of_question_info.text = getString(R.string.question_number_info, questionNumber, 10)

        val questionAnswerList = listOf(
                Triple(R.string.question_1_dummy, R.string.question_1_a_dummy, R.string.question_1_b_dummy),
                Triple(R.string.question_2_dummy, R.string.question_2_a_dummy, R.string.question_2_b_dummy))
        val (question, answerA, answerB) = questionAnswerList[questionNumber - 1]

        tv_question.text = getString(question)
        answer_a.text = getString(answerA)
        answer_b.text = getString(answerB)
    }

    override fun setLayout(): Int {
        return R.layout.activity_kuis
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        fun setIntent(context: Context, kuisId: Int, questionNumber: Int): Intent {
            val intent = Intent(context, KuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ID, kuisId)
            intent.putExtra(PantauConstants.Kuis.KUIS_NUMBER, questionNumber)
            return intent
        }
    }
}