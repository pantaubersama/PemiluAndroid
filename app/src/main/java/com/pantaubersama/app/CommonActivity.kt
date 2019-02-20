package com.pantaubersama.app

import android.app.ProgressDialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.snackBar

/**
 * Extend
 * */
abstract class CommonActivity : AppCompatActivity() {

    protected var progressDialog: ProgressDialog? = null
    protected var toolbar: Toolbar? = null
    protected abstract fun statusBarColor(): Int?

    @LayoutRes
    protected abstract fun setLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayout())
        initProgressDialog()
        fetchIntentExtra()
        setupUI(savedInstanceState)

        if (statusBarColor() != 0) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (statusBarColor() == R.color.white && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = ContextCompat.getColor(this, statusBarColor()!!)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else if (toolbar != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val toolbarColor = (toolbar?.background as ColorDrawable).color
            if (toolbarColor == -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = toolbarColor
            }
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.txt_mohon_tunggu))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    protected open fun fetchIntentExtra() {}

    protected abstract fun setupUI(savedInstanceState: Bundle?)

    protected fun setFragmentContainerId(): Int {
        return 0
    }

    fun setupToolbar(isBackButtonEnable: Boolean, title: String, @ColorRes color: Int, elevation: Float) {
        toolbar = findViewById<Toolbar>(R.id.toolbar)?.apply {
            setNavigationOnClickListener { onBackPressed() }
            this.title = title
            this.elevation = elevation
            setBackgroundColor(color(color))
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(isBackButtonEnable)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showProgressDialog(message: String) {
        progressDialog?.setMessage(message)

        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    open fun showError(throwable: Throwable) {
        window?.decorView?.findViewById<View>(android.R.id.content)?.snackBar(throwable.message!!)
//        Timber.e(throwable)
        throwable.printStackTrace()
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }
}
