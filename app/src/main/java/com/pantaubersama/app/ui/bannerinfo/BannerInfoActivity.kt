package com.pantaubersama.app.ui.bannerinfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.HtmlTagHandler
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_banner_info.*
import javax.inject.Inject

class BannerInfoActivity : BaseActivity<BannerInfoPresenter>(), BannerInfoView {

    @Inject
    override lateinit var presenter: BannerInfoPresenter

    private var infoType: Int? = null
    private var bannerInfo: BannerInfo? = null

    companion object {
        fun setIntent(context: Context, infoType: Int, bannerInfo: BannerInfo): Intent {
            val intent = Intent(context, BannerInfoActivity::class.java)
            intent.putExtra(PantauConstants.Extra.BANNER_INFO_TYPE, infoType)
            intent.putExtra(PantauConstants.Extra.BANNER_INFO_DATA, bannerInfo)
            return intent
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
        this.infoType = intent.getIntExtra(PantauConstants.Extra.BANNER_INFO_TYPE, 0)
        this.bannerInfo = intent.getSerializableExtra(PantauConstants.Extra.BANNER_INFO_DATA) as BannerInfo
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        btn_close.setOnClickListener {
            finish()
        }

        tv_banner_title.text = bannerInfo?.title

        val str = bannerInfo?.body
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv_banner_description.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), Html.FROM_HTML_MODE_LEGACY, null, HtmlTagHandler())
        } else {
            tv_banner_description.text = Html.fromHtml(HtmlTagHandler.customizeListTags(str), null, HtmlTagHandler())
        }

        iv_banner_image.loadUrl(bannerInfo?.image?.large?.url, R.color.gray_3)
        iv_banner_background.loadUrl(bannerInfo?.headerImage?.large?.url)

        tv_url_pantau.setOnClickListener { ChromeTabUtil(this).loadUrl(tv_url_pantau.text.toString()) }
    }

    override fun showBannerInfo(item: BannerInfo) {
        setResult(Activity.RESULT_OK)
        tv_banner_title.text = item.title
        tv_banner_description.text = item.body
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
}
