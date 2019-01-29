package com.pantaubersama.app.ui.profile.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.webkit.CookieManager
import androidx.core.content.ContextCompat
import com.extrainteger.symbolic.ui.SymbolicLoginButton
import com.facebook.* // ktlint-disable
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.setting.clusterundang.ClusterUndangActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.verified_layout.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.facebook.GraphRequest
import com.pantaubersama.app.data.model.user.VerificationStep
import com.pantaubersama.app.ui.profile.setting.tentangapp.TentangAppActivity
import com.pantaubersama.app.ui.profile.verifikasi.VerifikasiNavigator
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.visibleIf
import com.twitter.sdk.android.core.* // ktlint-disable
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User

class SettingActivity : BaseActivity<SettingPresenter>(), SettingView {

    @Inject
    override lateinit var presenter: SettingPresenter
    private var verifiedDialog: Dialog? = null
    private lateinit var callbackManager: CallbackManager
    private lateinit var permissions: MutableList<String>
    private lateinit var twitterAuthClient: TwitterAuthClient

    companion object {
        val EDIT_PROFILE = 1
        val UBAH_DATA_LAPOR = 3
        val VERIFIKASI = 4
        val BADGE = 5
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // OK
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_setting), R.color.white, 4f)
        onClickAction()
        getFacebookLoginSatus()
        setupFacebookLogin()
        getTwitterUserData()
        setupTwitterLogin()
        presenter.getProfile()
    }

    private fun getTwitterUserData() {
        if (TwitterCore.getInstance().sessionManager.activeSession != null) {
            presenter.getTwitterUserData()
            connect_twitter_label.text = getString(R.string.label_connected_as)
        } else {
            connect_twitter_label.text = getString(R.string.label_click_connect_twitter)
            twitter_login_text.text = getString(R.string.label_connect_with_twitter)
            twitter_login_icon.setImageDrawable(ContextCompat.getDrawable(this@SettingActivity, R.drawable.ic_twitter))
        }
    }

    override fun bindTwitterUserData(data: User?) {
        data?.screenName?.let { twitter_login_text.text = it }
        data?.profileImageUrlHttps?.let { twitter_login_icon.loadUrl(it) }
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal memuat data pengguna Twitter")
    }

    private fun setupTwitterLogin() {
        twitterAuthClient = TwitterAuthClient()
        setting_connect_twitter.setOnClickListener {
            if (TwitterCore.getInstance().sessionManager.activeSession == null) {
                twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
                    override fun success(result: Result<TwitterSession>) {
                        presenter.connectTwitter(PantauConstants.CONNECT.TWITTER, result.data.authToken.token, result.data.authToken.secret)
                    }

                    override fun failure(exception: TwitterException?) {
                        Timber.e(exception)
                        ToastUtil.show(this@SettingActivity, "Gagal login dengan Twitter")
                    }
                })
            } else {
                ConfirmationDialog
                        .Builder()
                        .with(this@SettingActivity)
                        .setDialogTitle("Disconnect Twitter")
                        .setAlert("Apakah Anda yakin untuk disconnect Twitter?")
                        .setCancelText("Batal")
                        .setOkText("Disconnect")
                        .addOkListener(object : ConfirmationDialog.DialogOkListener {
                            override fun onClickOk() {
                                presenter.disconnectSocialMedia(PantauConstants.CONNECT.TWITTER)
                            }
                        })
                        .show()
            }
        }
    }

    override fun onFailedConnectTwitter() {
        logoutTwitterSDK()
    }

    override fun showConnectedToTwitterAlert() {
        ToastUtil.show(this@SettingActivity, "Terhubung dengan Twitter")
        getTwitterUserData()
    }

    override fun showFailedToConnectTwitterAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal menghubungkan ke Twitter")
    }

    private fun getFacebookLoginSatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            facebook_connect_label.text = getString(R.string.label_connected_as)
            val request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken()
            ) { me, _ ->
                facebook_login_text.text = me?.getString("name")
                val request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + me.getString("id") + "/picture"
                ) {
                    try {
                        facebook_login_icon.loadUrl(it.jsonObject.getJSONObject("data").getString("url"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val params = Bundle()
                params.putBoolean("redirect", false)
                request.parameters = params
                request.executeAsync()
            }
            request.executeAsync()
        } else {
            facebook_connect_label.text = getString(R.string.label_click_connect_facebook)
            facebook_login_text.text = getString(R.string.label_connect_with_fb)
            facebook_login_icon.setImageDrawable(ContextCompat.getDrawable(this@SettingActivity, R.drawable.facebook))
        }
    }

    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        permissions = ArrayList()
        permissions.add("public_profile")
        permissions.add("email")
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                presenter.connectFacebook(PantauConstants.CONNECT.FACEBOOK, result?.accessToken?.token)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Timber.e(error?.localizedMessage)
                ToastUtil.show(this@SettingActivity, "Gagal mengubungkan ke Facebook")
            }
        })
        connect_fb.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                LoginManager.getInstance().logInWithReadPermissions(this@SettingActivity, permissions)
            } else {
                ConfirmationDialog
                        .Builder()
                        .with(this@SettingActivity)
                        .setDialogTitle("Disconnect Facebook")
                        .setAlert("Apakah Anda yakin untuk disconnect Facebook?")
                        .setCancelText("Batal")
                        .setOkText("Disconnect")
                        .addOkListener(object : ConfirmationDialog.DialogOkListener {
                            override fun onClickOk() {
                                presenter.disconnectSocialMedia(PantauConstants.CONNECT.FACEBOOK)
                            }
                        })
                        .show()
            }
        }
    }

    override fun onFailedConnectFacebook() {
        logoutFacebookSDK()
    }

    override fun onSuccessGetProfile(profile: Profile) {
        setting_verifikasi.setOnClickListener {
            if (profile.verified) {
                openVerifiedDialog(profile)
            } else {
                presenter.getStatusVerifikasi()
            }
        }
        if (profile.cluster != null && profile.isModerator) {
            setting_cluster_undang.setOnClickListener {
                val intent = Intent(this@SettingActivity, ClusterUndangActivity::class.java)
                intent.putExtra(PantauConstants.Cluster.CLUSTER_ID, profile.cluster?.id)
                startActivity(intent)
            }
        } else {
            ll_setting_cluster_undang.visibleIf(false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openVerifiedDialog(profile: Profile) {
        verifiedDialog = Dialog(this@SettingActivity)
        verifiedDialog?.setContentView(R.layout.verified_layout)
        verifiedDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = verifiedDialog?.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        verifiedDialog?.iv_user_avatar?.loadUrl(profile.avatar.url, R.drawable.ic_avatar_placeholder)
        verifiedDialog?.tv_user_name?.text = profile.name
        verifiedDialog?.user_username?.text = "@" + profile.username
        if (profile.verified) setVerified() else setUnverified()
        verifiedDialog?.show()
    }

    private fun setUnverified() {
        verifiedDialog?.verified_icon?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@SettingActivity, R.color.gray_dark_1), PorterDuff.Mode.MULTIPLY)
        verifiedDialog?.verified_text?.text = getString(R.string.txt_belum_verifikasi)
        verifiedDialog?.verified_text?.setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.gray_dark_1))
        verifiedDialog?.verified_button?.setBackgroundResource(R.drawable.rounded_outline_gray)
    }

    private fun setVerified() {
        verifiedDialog?.verified_icon?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@SettingActivity, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        verifiedDialog?.verified_text?.text = getString(R.string.txt_terverifikasi)
        verifiedDialog?.verified_text?.setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.colorAccent))
        verifiedDialog?.verified_button?.setBackgroundResource(R.drawable.rounded_outline_green)
    }

    override fun setLayout(): Int {
        return R.layout.activity_setting
    }

    override fun showLoading() {
        showProgressDialog("Mohon tunggu")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun showConnectedToFacebookAlert() {
        ToastUtil.show(this@SettingActivity, "Terhubung dengan Facebook")
        getFacebookLoginSatus()
    }

    override fun showFailedToConnectFacebookAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal menghubungkan ke Facebook")
    }

    fun onClickAction() {
        setting_ubah_profil.setOnClickListener {
            val intent = Intent(this@SettingActivity, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE)
        }
        setting_ubah_sandi.setOnClickListener {
            SymbolicLoginButton.loadPage(this, BuildConfig.SYMBOLIC_URL + "/users/edit", BuildConfig.SYMBOLIC_URL + "/")
        }
        setting_ubah_data_lapor.setOnClickListener {
            val intent = Intent(this@SettingActivity, UbahDataLaporActivity::class.java)
            startActivityForResult(intent, UBAH_DATA_LAPOR)
        }
        setting_badge.setOnClickListener {
            val intent = Intent(this@SettingActivity, BadgeActivity::class.java)
            startActivityForResult(intent, BADGE)
        }
        setting_pusat_bantuan.setOnClickListener {
            ChromeTabUtil(this@SettingActivity).forceLoadUrl(PantauConstants.Profile.URL_PUSAT_BANTUAN)
        }
        setting_pedoman_komunitas.setOnClickListener {
            ChromeTabUtil(this@SettingActivity).forceLoadUrl(PantauConstants.Profile.URL_PANDUAN_KOMUNITAS)
        }
        setting_tentang.setOnClickListener {
            startActivity(Intent(this@SettingActivity, TentangAppActivity::class.java))
        }
        setting_berikan_nilai.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
            }
        }
        setting_bagikan_aplikasi.setOnClickListener {
            ShareUtil.shareApp(this@SettingActivity, "https://play.google.com/store/apps/details?id=$packageName")
        }
        setting_logout.setOnClickListener {
            logoutDialog()
        }
    }

    override fun goToLogin() {
        logoutFacebookSDK()
        logoutTwitterSDK()
        SymbolicLoginButton.logOut(this@SettingActivity)
        val intent = Intent(this@SettingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_PROFILE) {
                setResult(Activity.RESULT_OK)
            }
        }
    }

    fun logoutDialog() {
        ConfirmationDialog
                .Builder()
                .with(this@SettingActivity)
                .setDialogTitle(getString(R.string.title_keluar_aplikasi))
                .setAlert(getString(R.string.logout_alert))
                .setCancelText(getString(R.string.batal_action))
                .setOkText(getString(R.string.label_keluar))
                .addOkListener(object : ConfirmationDialog.DialogOkListener {
                    override fun onClickOk() {
                        presenter.revokeFirebaseToken()
                    }
                })
                .show()
    }

    override fun onSuccessRevokeFirebaseToken() {
        presenter.logOut(BuildConfig.PANTAU_CLIENT_ID, BuildConfig.PANTAU_CLIENT_SECRET)
    }

    override fun showLogoutFailedAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal logout")
    }

    override fun showSuccessDisconnectFacebookAlert() {
        ToastUtil.show(this@SettingActivity, "Disconnect Facebook berhasil")
    }

    override fun showSuccessDisconnectTwitterAlert() {
        ToastUtil.show(this@SettingActivity, "Disconnect Twitter berhasil")
    }

    override fun showFailedDisconnectFacebookAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal disconnect Facebook")
    }

    override fun showFailedDisconnectTwitterAlert() {
        ToastUtil.show(this@SettingActivity, "Gagal disconnect Twitter")
    }

    override fun showVerifikasiScreen(step: VerificationStep) {
        VerifikasiNavigator.start(this, step)
    }

    override fun showFailedGetVerifikasi() {
        ToastUtil.show(this, "Gagal mendapatkan status verifikasi")
    }

    override fun logoutFacebookSDK() {
        CookieManager.getInstance().removeAllCookies(null)
        LoginManager.getInstance().logOut()
        getFacebookLoginSatus()
    }

    override fun logoutTwitterSDK() {
        CookieManager.getInstance().removeAllCookies(null)
        TwitterCore.getInstance().sessionManager.clearActiveSession()
        getTwitterUserData()
    }
}