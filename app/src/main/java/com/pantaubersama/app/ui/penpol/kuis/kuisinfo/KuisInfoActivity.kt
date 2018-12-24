package com.pantaubersama.app.ui.penpol.kuis.kuisinfo

import android.text.Html
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.HtmlTagHandler
import kotlinx.android.synthetic.main.activity_kuis_info.*

class KuisInfoActivity : BaseActivity<BasePresenter<*>>() {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun setupUI() {
        close_button.setOnClickListener {
            finish()
        }
        val inputStream = assets.open("kuis_hint.html")
        val size = inputStream.available()

        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val str = String(buffer)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            question_long_hint.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
        } else {
            question_long_hint.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), null, HtmlTagHandler())
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_kuis_info
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
