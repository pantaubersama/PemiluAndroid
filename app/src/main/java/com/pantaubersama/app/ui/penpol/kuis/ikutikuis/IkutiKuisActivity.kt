package com.pantaubersama.app.ui.penpol.kuis.ikutikuis

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_ikuti_kuis.*

class IkutiKuisActivity : BaseActivity<BasePresenter<*>>() {
    private var kuisId: Int = 0

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        kuisId = intent.getIntExtra(PantauConstants.Kuis.KUIS_ID, 0)
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
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

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
