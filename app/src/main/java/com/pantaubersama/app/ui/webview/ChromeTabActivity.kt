package com.pantaubersama.app.ui.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_BROADCAST_URL
import timber.log.Timber

class ChromeTabActivity : CommonActivity() {
    private var url: String? = null
    private var isCustomTabsOpened = false

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_chrome_tab

    companion object {
        fun setIntent(context: Context, url: String): Intent {
            val intent = Intent(context, ChromeTabActivity::class.java)
            intent.putExtra(EXTRA_BROADCAST_URL, url)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.apply {
            getStringExtra(EXTRA_BROADCAST_URL)?.let { url = it }
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        Timber.d("ChromeTabActivity setupUI")
        url?.let { launchCustomTabs() }
    }

    private fun launchCustomTabs() {
        isCustomTabsOpened = true
        ChromeTabUtil(this).forceLoadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("ChromeTabActivity onResume")
        if (isCustomTabsOpened) {
            Timber.d("ChromeTabActivity onResume isCustomTabsOpened")
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        Timber.d("ChromeTabActivity onBackPressed")
        if (isTaskRoot) {
            Timber.d("ChromeTabActivity isTaskRoot")
            val intent = Intent(this@ChromeTabActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        super.onBackPressed()
    }
}
