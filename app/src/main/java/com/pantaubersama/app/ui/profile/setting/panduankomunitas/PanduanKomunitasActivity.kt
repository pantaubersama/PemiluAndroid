package com.pantaubersama.app.ui.profile.setting.panduankomunitas

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_panduan_komunitas.*
import javax.inject.Inject

class PanduanKomunitasActivity : BaseActivity<PanduanKomunitasPresenter>(), PanduangKomunitasView {

    @Inject
    override lateinit var presenter: PanduanKomunitasPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            panduan_komunitas_panduan.text = Html.fromHtml("Ini nanti teks statis, pake html view aja\n" +
                    "Pantau Bersama diharapkan cluster is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown " +
                    "printer took a galley of type and scrambled it to make a type specimen book.", Html.FROM_HTML_MODE_COMPACT)
        } else {
            panduan_komunitas_panduan.text = Html.fromHtml("Ini nanti teks statis, pake html view aja\n" +
                    "Pantau Bersama diharapkan cluster is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown " +
                    "printer took a galley of type and scrambled it to make a type specimen book.")
        }
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_panduan_komunitas
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }

    private fun onClickAction() {
        panduan_komunitas_close.setOnClickListener {
            finish()
        }
    }
}
