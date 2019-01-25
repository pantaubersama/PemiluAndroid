package com.pantaubersama.app.ui.penpol.kuis.result

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.facebook.internal.NativeProtocol.EXTRA_USER_ID
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_kuis_user_result.*
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
        constraint_layout_content.visibleIf(true)
        tv_kuis_result.text = spannable {
            +"Total Kecenderungan ${kuisUserResult.meta.finished} Dari ${kuisUserResult.meta.total} Quiz,\n"
            textColor(color(R.color.black_3)) { +userName }
            +" lebih suka jawaban dari Paslon no ${kuisUserResult.team.id}"
        }.toCharSequence()
        iv_paslon.loadUrl(kuisUserResult.team.avatar)
        tv_percentage.text = "%d%%".format(kuisUserResult.percentage.roundToInt())
        tv_paslon_name.text = kuisUserResult.team.title
        btn_share.setOnClickListener {
//            ShareUtil.shareItem(this, kuisUserResult)
            takeScreenShot()
        }
    }

    private fun takeScreenShot() {
        showProgressDialog("Tunggu yakk ...")
        val rootView = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setupToolbar(false, "", R.color.white, 0f)
        rootView.draw(canvas)
        shareOnWhatsApp(ImageUtil.getImageFile(bitmap))
    }

    private fun shareOnWhatsApp(imageFile: File) {
        val imageUri = Uri.parse(imageFile.absolutePath)
        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)

//        shareIntent.setPackage("com.whatsapp")
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "eh iso")
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.setType("*/*")
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        setupToolbar(true, "", R.color.white, 0f)
        dismissProgressDialog()

        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            ToastUtil.show(this, "Oops.. ada yang salah nih")
        }
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
}