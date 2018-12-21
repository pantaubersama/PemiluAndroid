package com.pantaubersama.app.ui.penpol.tanyakandidat.tanyakandidatinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.HtmlTagHandler
import kotlinx.android.synthetic.main.activity_tanya_kandidat_banner.*

class TanyaKandidatBannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tanya_kandidat_banner)
        close_button.setOnClickListener {
            finish()
        }
        val inputStream = assets.open("tanya_hint.html")
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
}
