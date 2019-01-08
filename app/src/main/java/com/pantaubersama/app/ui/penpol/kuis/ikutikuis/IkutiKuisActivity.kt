package com.pantaubersama.app.ui.penpol.kuis.ikutikuis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_ikuti_kuis.*

class IkutiKuisActivity : CommonActivity() {
    private var kuisId: String = ""

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        kuisId = intent.getStringExtra(PantauConstants.Kuis.KUIS_ID)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        start_quiz_action.setOnClickListener {
            val intent = KuisActivity.setIntent(this, kuisId, 1)
            startActivity(intent)
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_ikuti_kuis
    }

    companion object {
        fun setIntent(context: Context, kuisId: String): Intent {
            val intent = Intent(context, KuisActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ID, kuisId)
            return intent
        }
    }
}
