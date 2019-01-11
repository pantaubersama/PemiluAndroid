package com.pantaubersama.app.ui.penpol.kuis.ikutikuis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_ikuti_kuis.*

class IkutiKuisActivity : CommonActivity() {

    private lateinit var kuisItem: KuisItem

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        kuisItem = intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM) as KuisItem
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        iv_kuis_image.loadUrl(kuisItem.image.url)
        quiz_title.text = kuisItem.title
        question_count.text = "%d Pertanyaan".format(kuisItem.kuisQuestionsCount)
        quiz_long_hint.text = kuisItem.description
        start_quiz_action.setOnClickListener {
            val intent = KuisActivity.setIntent(this, kuisItem, true).apply {
                addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_ikuti_kuis
    }

    companion object {
        fun setIntent(context: Context, kuisItem: KuisItem): Intent {
            val intent = Intent(context, IkutiKuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ITEM, kuisItem)
            return intent
        }
    }
}
