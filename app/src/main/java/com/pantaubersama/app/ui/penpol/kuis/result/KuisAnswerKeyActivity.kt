package com.pantaubersama.app.ui.penpol.kuis.result

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.OffsetItemDecoration
import com.pantaubersama.app.utils.extensions.dip
import kotlinx.android.synthetic.main.activity_kuis_answer_key.*

class KuisAnswerKeyActivity : CommonActivity() {

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_kuis_answer_key

    override fun setupUI(savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = KuisAnswerKeyAdapter().apply { items = 10 }
        recycler_view.addItemDecoration(OffsetItemDecoration(dip(16)))

        close_button.setOnClickListener { onBackPressed() }
    }
}