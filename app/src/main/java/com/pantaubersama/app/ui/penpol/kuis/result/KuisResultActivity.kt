package com.pantaubersama.app.ui.penpol.kuis.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisResult
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_QUIZ_PARTICIPATION_ID
import com.pantaubersama.app.utils.PantauConstants.Permission.WRITE_FILE_PERMISSION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_HASIL_KUIS_PATH
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_result.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToInt

class KuisResultActivity : BaseActivity<KuisResultPresenter>(), KuisResultView {

    @Inject
    override lateinit var presenter: KuisResultPresenter

    private var kuisItem: KuisItem? = null
    private var quizParticipationId: String? = null

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_kuis_result

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(PantauConstants.Kuis.KUIS_ITEM)?.let { kuisItem = it as KuisItem }
        intent.getStringExtra(EXTRA_QUIZ_PARTICIPATION_ID)?.let { quizParticipationId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        if (kuisItem != null) {
            kuisItem?.let { presenter.getKuisResult(it.id) }
        } else {
            quizParticipationId?.let { presenter.getKuisResultByQuizParticipationId(it) }
            btn_see_answers.visibleIf(false)
        }
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
        constraint_layout_content.visibleIf(false)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showResult(kuisResult: KuisResult, userName: String) {
        quizParticipationId = kuisResult.quizParticipation.id
        tv_kuis_result.text = spannable {
            +"Dari hasil pilihan di Quiz ${kuisResult.title},\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${kuisResult.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(kuisResult.team.avatar)
        tv_percentage.text = "%d%%".format(kuisResult.percentage.roundToInt())
        tv_paslon_name.text = kuisResult.team.title
        btn_share.setOnClickListener {
//            ShareUtil.shareItem(this, kuisResult)
            takeScreenShot()
        }
        btn_see_answers.setOnClickListener {
            kuisItem?.let { kuisItem -> startActivity(KuisSummaryActivity.setIntent(this, kuisItem)) }
        }

        constraint_layout_content.visibleIf(true)
    }

    @AfterPermissionGranted(RC_ASK_PERMISSIONS)
    private fun takeScreenShot() {
        if (EasyPermissions.hasPermissions(this, *WRITE_FILE_PERMISSION)) {
            showProgressDialog("Tunggu yakk ...")
            setupToolbar(false, "", R.color.white, 0f)
            btn_see_answers.visibleIf(false)
            share(ImageUtil.getScreenshotAsFile(window.decorView.rootView))
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_ASK_PERMISSIONS, *WRITE_FILE_PERMISSION)
                    .setRationale(getString(R.string.izinkan_akses_file))
                    .setPositiveButtonText(getString(R.string.ok))
                    .setNegativeButtonText(getString(R.string.batal))
                    .build()
            )
        }
    }

    private fun share(imageFile: File) {
        setupToolbar(true, "", R.color.white, 0f)
        btn_see_answers.visibleIf(true)
        dismissProgressDialog()
        ShareUtil.shareImage(
            this,
            "Kamu sudah ikut? Aku sudah dapat hasilnya \uD83D\uDE0E %s".format(BuildConfig.PANTAU_WEB_URL + SHARE_HASIL_KUIS_PATH + quizParticipationId), imageFile)
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(PantauConstants.Kuis.KUIS_REFRESH, false)) {
            setResult(Activity.RESULT_OK)
        } else if (isTaskRoot) {
            val intent = Intent(this@KuisResultActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        super.onBackPressed()
    }

    companion object {
        fun setIntent(context: Context, kuisItem: KuisItem, refreshOnReturn: Boolean = false): Intent {
            val intent = Intent(context, KuisResultActivity::class.java)
            intent.putExtra(PantauConstants.Kuis.KUIS_ITEM, kuisItem)
            intent.putExtra(PantauConstants.Kuis.KUIS_REFRESH, refreshOnReturn)
            return intent
        }

        fun setIntent(context: Context, quizParticipationId: String): Intent {
            val intent = Intent(context, KuisResultActivity::class.java)
            intent.putExtra(EXTRA_QUIZ_PARTICIPATION_ID, quizParticipationId)
            return intent
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}