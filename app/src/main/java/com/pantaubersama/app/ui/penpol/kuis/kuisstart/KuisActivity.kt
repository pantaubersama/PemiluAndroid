package com.pantaubersama.app.ui.penpol.kuis.kuisstart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_kuis.*

class KuisActivity : CommonActivity() {

    private var questionNumber: Int = 0

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        questionNumber = intent.getIntExtra(PantauConstants.Kuis.KUIS_NUMBER, 0)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        back_icon.setOnClickListener {
            finish()
        }

        with(resources.obtainTypedArray(R.array.bg_kuis)) {
            val bgResource = getResourceId(questionNumber - 1, R.drawable.kuis_background_1)
            iv_background.setImageResource(bgResource)
            recycle()
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

    companion object {
        fun setIntent(context: Context, kuisId: Int, questionNumber: Int): Intent {
            val intent = Intent(context, KuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ID, kuisId)
            intent.putExtra(PantauConstants.Kuis.KUIS_NUMBER, questionNumber)
            return intent
        }
    }
}