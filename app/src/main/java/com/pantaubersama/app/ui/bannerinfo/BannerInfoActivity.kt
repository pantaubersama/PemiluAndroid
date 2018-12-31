package com.pantaubersama.app.ui.bannerinfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.utils.HtmlTagHandler
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_banner_info.*
import javax.inject.Inject

class BannerInfoActivity : BaseActivity<BannerInfoPresenter>(), BannerInfoView {

    @Inject
    lateinit var interactor: BannerInfoInteractor
    private var infoType: Int? = null

    companion object {
        fun setIntent(context: Context, infoType: Int): Intent {
            val intent = Intent(context, BannerInfoActivity::class.java)
            intent.putExtra(PantauConstants.Extra.BANNER_INFO_TYPE, infoType)
            return intent
        }
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
        this.infoType = intent.getIntExtra(PantauConstants.Extra.BANNER_INFO_TYPE, 0)
    }

    override fun initPresenter(): BannerInfoPresenter? {
        return BannerInfoPresenter(interactor)
    }

    override fun setupUI() {
        btn_close.setOnClickListener {
            finish()
        }

        tv_banner_title.text = when (infoType) {
            PantauConstants.Extra.TYPE_PILPRES -> getString(R.string.txt_pilpres)
            PantauConstants.Extra.TYPE_JANPOL -> getString(R.string.txt_tab_janji_politik)
            PantauConstants.Extra.TYPE_TANYA_KANDIDAT -> getString(R.string.tanya_kandidat_label)
            else -> getString(R.string.kuis_label)
        }

        val inputStream = assets.open(when (infoType) {
            0 -> "tanya_hint.html"
            1 -> "tanya_hint.html"
            2 -> "tanya_hint.html"
            else -> "kuis_hint.html"
        })
        val size = inputStream.available()

        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val str = String(buffer)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv_banner_desricption.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
        } else {
            tv_banner_desricption.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), null, HtmlTagHandler())
        }

        getBannerInfo()
    }

    private fun getBannerInfo() {
        val pageName = when (infoType) {
            PantauConstants.Extra.TYPE_PILPRES -> PantauConstants.BANNER_PILPRES
            PantauConstants.Extra.TYPE_JANPOL -> PantauConstants.BANNER_JANPOL
            PantauConstants.Extra.TYPE_TANYA_KANDIDAT -> PantauConstants.BANNER_TANYA
            else ->  PantauConstants.BANNER_KUIS
        }
        presenter?.getBannerInfo(pageName)
    }

    override fun showBannerInfo(item: BannerInfo) {
//        Glide.with(this).load(item.headerImage).into(iv_banner_background)
        setResult(Activity.RESULT_OK)
        tv_banner_title.text = item.title
        tv_banner_desricption.text = item.body
    }

    override fun setLayout(): Int {
        return R.layout.activity_banner_info
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        rl_banner_container.visibility = View.GONE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
        rl_banner_container.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        (application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}
