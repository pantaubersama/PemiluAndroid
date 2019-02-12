package com.pantaubersama.app.ui.penpol.kuis.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.penpol.kuis.ikutikuis.IkutiKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_KUIS
import com.pantaubersama.app.utils.PantauConstants.Kuis.KUIS_ID
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.setBackgroundTint
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_kuis.*
import kotlinx.android.synthetic.main.item_kuis.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class DetailKuisActivity : BaseActivity<DetailKuisPresenter>(), DetailKuisView {

    @Inject
    override lateinit var presenter: DetailKuisPresenter

    private lateinit var quizId: String

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_detail_kuis

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        fun setIntent(context: Context, quizId: String): Intent {
            val intent = Intent(context, DetailKuisActivity::class.java)
            intent.putExtra(KUIS_ID, quizId)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(KUIS_ID)?.let { this.quizId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        btn_close.setOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        presenter.getQuizById(quizId)
    }

    override fun showQuiz(item: KuisItem) {
        layout_item_kuis.visibleIf(true)
        val (buttonText, buttonColor) = when (item.state) {
            KuisState.NOT_PARTICIPATING -> "IKUTI" to R.color.orange
            KuisState.FINISHED -> "HASIL" to R.color.colorAccent
            KuisState.IN_PROGRESS -> "LANJUT" to R.color.red_2
        }
        iv_kuis_image.loadUrl(item.image.url)
        tv_kuis_title.text = item.title
        tv_kuis_count.text = "${item.kuisQuestionsCount} Pertanyaan"
        btn_kuis_open.text = "$buttonText >>"
        btn_kuis_open.setBackgroundTint(buttonColor)
        btn_kuis_open.setOnClickListener {
            val intent = when (item.state) {
                KuisState.NOT_PARTICIPATING -> IkutiKuisActivity.setIntent(this, item)
                KuisState.IN_PROGRESS -> KuisActivity.setIntent(this, item)
                KuisState.FINISHED -> KuisResultActivity.setIntent(this, item)
            }
            startActivityForResult(intent, PantauConstants.RequestCode.RC_REFRESH_KUIS_ON_RESULT)
        }
        btn_share.setOnClickListener { ShareUtil.shareItem(this, item) }
    }

    override fun onFailedGetQuiz() {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        layout_item_kuis.visibleIf(false)
        view_fail_state.enableLottie(false, lottie_fail_state)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = HomeActivity.setIntentByOpenedTab(this, EXTRA_TYPE_KUIS)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        super.onBackPressed()
    }
}