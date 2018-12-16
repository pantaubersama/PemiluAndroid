package com.pantaubersama.app.base

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import com.pantaubersama.app.R
import java.lang.RuntimeException

/**
 * @author edityomurti on 14/12/2018 17:33
 */

abstract class BaseActivity<P : BasePresenter<BaseView>> : AppCompatActivity() {
    protected var progressDialog: ProgressDialog? = null
    protected var presenter: P? = null
    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(setLayout())
        fetchIntentExtra()
        if (setFragment() != null) {
            supportFragmentManager.beginTransaction()
                    .replace(setFragmentContainerId(), setFragment()!!)
                    .commit()
        } else {
            presenter = initPresenter()
            if (presenter != null) {
                presenter!!.attach()
            }
        }
        initProgressDialog()
        setupUI()

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(statusBarColor())
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.detach()
        }

        if (progressDialog!!.isShowing) {
            dismissProgressDialog()
        }
        super.onDestroy()
    }

    protected abstract fun statusBarColor(): Int

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
    protected fun initPresenter(): P? {
        if (setFragment() != null) {
            return null
        } else {
            throw RuntimeException("need to override initPresenter()")
        }
    }

    protected abstract fun setupUI()

    protected fun setFragment(): Fragment? {
        return null
    }

    protected fun setFragmentContainerId(): Int {
        return 0
    }

    @LayoutRes
    protected abstract fun setLayout(): Int

    fun setupToolbar(isBackButtonEnable: Boolean, title: String, @DrawableRes color: Int, elevation: Float) {
        toolbar = findViewById(R.id.toolbar)

        if (toolbar != null) {
            toolbar!!.setNavigationOnClickListener { view -> onBackPressed() }
            toolbar!!.title = title
            toolbar!!.background = resources.getDrawable(color)
            toolbar!!.elevation = elevation
            setSupportActionBar(toolbar)

            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(isBackButtonEnable)
//                supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_))
            }
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
}