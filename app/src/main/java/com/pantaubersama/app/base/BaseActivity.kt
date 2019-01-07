package com.pantaubersama.app.base

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
import com.pantaubersama.app.R
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.snackBar
import timber.log.Timber

/**
 * @author edityomurti on 14/12/2018 17:33
 */

abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity(), BaseView {
    protected var progressDialog: ProgressDialog? = null
    protected var presenter: P? = null
    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection(createActivityComponent())
        super.onCreate(savedInstanceState)
        // prevent all crash
//        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
//            Log.e("Error" + Thread.currentThread().stackTrace[2], paramThrowable.localizedMessage)
//        }
        setContentView(setLayout())
        fetchIntentExtra()
        presenter = initPresenter()
        if (presenter != null) {
            presenter!!.attach(this)
        }
        initProgressDialog()
        setupUI(savedInstanceState)

        if (statusBarColor() != 0) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, statusBarColor()!!)
            if (statusBarColor() == R.color.white && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else if (toolbar != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            var toolbarColor = (toolbar?.background as ColorDrawable).color
            window.statusBarColor = toolbarColor
            if (toolbarColor == -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    protected open fun initInjection(activityComponent: ActivityComponent) {}

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.detach()
        }

        if (progressDialog!!.isShowing) {
            dismissProgressDialog()
        }
        super.onDestroy()
    }

    protected abstract fun statusBarColor(): Int?

    protected abstract fun fetchIntentExtra()

    protected fun initProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(getString(R.string.txt_mohon_tunggu))
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    /**
     * optional to override when using presenter on fragment
     *
     * @return view presenter
     */
    protected abstract fun initPresenter(): P?

    protected abstract fun setupUI(savedInstanceState: Bundle?)

    protected fun setFragmentContainerId(): Int {
        return 0
    }

    @LayoutRes
    protected abstract fun setLayout(): Int

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

        if (!progressDialog?.isShowing!!) {
            progressDialog!!.show()
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    override fun showError(throwable: Throwable) {
        window?.decorView?.findViewById<View>(android.R.id.content)?.snackBar(throwable.message!!)
        Timber.e(throwable)
    }

    private fun createActivityComponent(): ActivityComponent {
        return (application as BaseApp).appComponent
                .withActivityComponent(ActivityModule(this))
    }
}