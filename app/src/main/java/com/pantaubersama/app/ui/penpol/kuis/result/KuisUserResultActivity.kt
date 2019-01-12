package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_user_result.*
import javax.inject.Inject

class KuisUserResultActivity : BaseActivity<KuisUserResultPresenter>(), KuisUserResultView {

    @Inject
    override lateinit var presenter: KuisUserResultPresenter

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.activity_kuis_user_result
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        presenter.getKuisUserResult()
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showKuisUserResult(kuisUserResult: KuisUserResult, userName: String) {
        tv_kuis_result.text = spannable {
            +"Total Kecenderungan ${kuisUserResult.meta.finished} Dari ${kuisUserResult.meta.total} Kuis,\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${kuisUserResult.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(kuisUserResult.team.avatar)
        tv_percentage.text = "%.2f%%".format(kuisUserResult.percentage)
        tv_paslon_name.text = kuisUserResult.team.title
        btn_share.setOnClickListener {
            shareKuis(kuisUserResult)
        }
    }

    private fun shareKuis(kuisUserResult: KuisUserResult) {
        val targetedShareIntents: MutableList<Intent> = ArrayList()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = this?.packageManager?.queryIntentActivities(shareIntent, 0)
        if (!resInfo!!.isEmpty()) {
            for (resolveInfo in resInfo) {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Pantau")
                sendIntent.putExtra(Intent.EXTRA_TEXT, "pantau.co.id" + "/share/kecenderungan/" + kuisUserResult.team.id)
                sendIntent.type = "text/plain"
                if (!resolveInfo.activityInfo.packageName.contains("pantaubersama")) {
                    sendIntent.`package` = resolveInfo.activityInfo.packageName
                    targetedShareIntents.add(sendIntent)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Bagikan dengan")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())
            startActivity(chooserIntent)
        }
    }
}
