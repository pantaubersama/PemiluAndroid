package com.pantaubersama.app.ui.splashscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.onboarding.OnboardingActivity
import com.pantaubersama.app.ui.penpol.kuis.detail.DetailKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisUserResultActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.widget.UpdateAppDialog
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Networking.INVITATION_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_BADGE_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_HASIL_KUIS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_JANPOL_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KECENDERUNGAN_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KUIS_PATH
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_TANYA_PATH
import com.pantaubersama.app.utils.extensions.enableLottie
import kotlinx.android.synthetic.main.activity_splash_screen.*
import javax.inject.Inject

class SplashScreenActivity : BaseActivity<SplashScreenPresenter>(), SplashScreenView {
    private var urlAction: String? = null
    private var urlData: String? = null
    private var urlPath: String? = null

    @Inject
    override lateinit var presenter: SplashScreenPresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        if (intent != null) {
            urlAction = intent.action
            urlData = intent.data?.toString()
            urlPath = intent.data?.path
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        presenter.getOnboardingStatus()
    }

    override fun isOnboardingComplete(complete: Boolean) {
        if (complete) {
            presenter.checkAppVersion(BuildConfig.VERSION_CODE)
        } else {
            val intent = Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onForceUpdateAvailable() {
        val dialog = UpdateAppDialog(this, object : UpdateAppDialog.DialogListener {
            override fun onClickUpdate() {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
                }
            }
        })
        dialog.show()
    }

    override fun goToHome() {
        when {
            urlPath.isNullOrEmpty() -> {
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
            urlPath?.contains(INVITATION_PATH)!! -> {
                val intent = Intent(this@SplashScreenActivity, ProfileActivity::class.java)
                intent.putExtra(PantauConstants.URL, urlData)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            urlPath?.contains(SHARE_JANPOL_PATH)!! && !urlPath?.substringAfter(SHARE_JANPOL_PATH).isNullOrEmpty() -> {
                val janpolId = urlPath?.substringAfter(SHARE_JANPOL_PATH)
                val intent = DetailJanjiPolitikActivity.setIntent(this, janpolId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
//            urlPath?.contains(SHARE_FEEDS_PATH)!! && !urlPath?.substringAfter(SHARE_FEEDS_PATH).isNullOrEmpty() -> {
//                ChromeTabUtil(this).forceLoadUrl(urlData)
//                finish()
//            }
            urlPath?.contains(SHARE_TANYA_PATH)!! && !urlPath?.substringAfter(SHARE_TANYA_PATH).isNullOrEmpty() -> {
                val questionId = urlPath?.substringAfter(SHARE_TANYA_PATH)
                val intent = DetailTanyaKandidatActivity.setIntent(this, questionId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            urlPath?.contains(SHARE_KECENDERUNGAN_PATH)!! && !urlPath?.substringAfter(SHARE_KECENDERUNGAN_PATH).isNullOrEmpty() -> {
                val userId = urlPath?.substringAfter(SHARE_KECENDERUNGAN_PATH)
                val intent = KuisUserResultActivity.setIntent(this, userId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            urlPath?.contains(SHARE_HASIL_KUIS_PATH)!! && !urlPath?.substringAfter(SHARE_HASIL_KUIS_PATH).isNullOrEmpty() -> {
                val quizParticipationId = urlPath?.substringAfter(SHARE_HASIL_KUIS_PATH)
                val intent = KuisResultActivity.setIntent(this, quizParticipationId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            urlPath?.contains(SHARE_KUIS_PATH)!! && !urlPath?.substringAfter(SHARE_KUIS_PATH).isNullOrEmpty() -> {
                val quizId = urlPath?.substringAfter(SHARE_KUIS_PATH)
                val intent = DetailKuisActivity.setIntent(this, quizId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            urlPath?.contains(SHARE_BADGE_PATH)!! && !urlPath?.substringAfter(SHARE_BADGE_PATH).isNullOrEmpty() -> {
                val achievedId = urlPath?.substringAfter(SHARE_BADGE_PATH)
                val intent = DetailBadgeActivity.setIntent(this, achievedId!!)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else -> {
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }

    override fun goToLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        urlData?.let { if (it.contains(INVITATION_PATH)) intent.putExtra(PantauConstants.URL, it) }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun setLayout(): Int {
        return R.layout.activity_splash_screen
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }
}
