package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.facebook.internal.NativeProtocol.EXTRA_USER_ID
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants.Permission.WRITE_FILE_PERMISSION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_SHARE
import com.pantaubersama.app.utils.PantauConstants.Share.SHARE_KECENDERUNGAN_PATH
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_user_result.*
import kotlinx.android.synthetic.main.layout_share_kuis_user_result.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToInt

class KuisUserResultActivity : BaseActivity<KuisUserResultPresenter>(), KuisUserResultView {

    @Inject
    override lateinit var presenter: KuisUserResultPresenter

    private var userId: String? = null

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int = R.layout.activity_kuis_user_result

    companion object {
        fun setIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, KuisUserResultActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(EXTRA_USER_ID)?.let { this.userId = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 0f)

        if (userId == null) {
            presenter.getKuisUserResult()
        } else {
            userId?.let { presenter.getKuisUserResultByUserId(it) }
        }
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
        constraint_layout_content.visibleIf(false)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showKuisUserResult(kuisUserResult: KuisUserResult, userName: String) {
        userId = kuisUserResult.user.id
        constraint_layout_content.visibleIf(true)
        val resultSummary = spannable {
            +"Total Kecenderungan ${kuisUserResult.meta.finished} Dari ${kuisUserResult.meta.total} Quiz,\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${kuisUserResult.team.id}"
        }.toCharSequence()
        resultSummary.let {
            tv_kuis_result.text = it
            tv_kuis_result_share.text = it
        }
        kuisUserResult.team.avatar.let {
            iv_paslon.loadUrl(it)
            iv_paslon_share.loadUrl(it)
        }
        "%d%%".format(kuisUserResult.percentage.roundToInt()).let {
            tv_percentage.text = it
            tv_percentage_share.text = it
        }
        kuisUserResult.team.title.let {
            tv_paslon_name.text = it
            tv_paslon_name_share.text = it
        }

        btn_share.setOnClickListener {
            takeScreenShot()
        }
    }

    @AfterPermissionGranted(RC_ASK_PERMISSIONS)
    private fun takeScreenShot() {
        if (EasyPermissions.hasPermissions(this, *WRITE_FILE_PERMISSION)) {
            showProgressDialog("Tunggu yakk ...")
            layout_share_user_kuis_result.visibleIf(true)
            layout_share_user_kuis_result.post {
                share(ImageUtil.getScreenshotAsFile(this@KuisUserResultActivity, layout_share_user_kuis_result))
            }
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
        val imageUri = Uri.parse(imageFile.absolutePath)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "SHARE")
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        dismissProgressDialog()
        ShareUtil.shareImage(this, "Hmm.. Ternyata begini kecenderunganku \uD83D\uDC40 #PantauBersama %s".format(BuildConfig.PANTAU_WEB_URL + SHARE_KECENDERUNGAN_PATH + userId), imageFile)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (this.isTaskRoot) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SHARE -> layout_share_user_kuis_result.visibleIf(false)
        }
    }
}