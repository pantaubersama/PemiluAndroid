package com.pantaubersama.app.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import timber.log.Timber
import java.util.Objects

/**
 * @author edityomurti on 18/12/2018 01:03
 */
abstract class BaseFragment<P : BasePresenter<*>> : Fragment(), BaseView {
    protected var progressDialog: ProgressDialog? = null
    protected var presenter: P? = null
    protected var toolbar: Toolbar? = null
    private var mView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjection()
        presenter = initPresenter()
        if (presenter != null) {
            presenter?.attach(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(setLayout(), container, false)
        initProgressDialog()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    protected abstract fun initPresenter(): P?

    protected open fun initInjection() {}

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage(getString(R.string.txt_mohon_tunggu))
    }

    protected abstract fun initView(view: View)

    @LayoutRes
    protected abstract fun setLayout(): Int

    fun setupToolbar(isBackButtonEnable: Boolean, title: String, @DrawableRes color: Int, elevation: Float) {
        toolbar = mView?.findViewById(R.id.toolbar)

        if (toolbar != null) {
            toolbar!!.setNavigationOnClickListener { Objects.requireNonNull(activity)?.onBackPressed() }
            toolbar!!.title = title
            toolbar!!.background = resources.getDrawable(color)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            if ((activity as AppCompatActivity).supportActionBar != null) {
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(isBackButtonEnable)
            }

            toolbar!!.elevation = elevation
        }
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter?.detach()
        }
        super.onDestroy()
    }

    override fun showError(throwable: Throwable) {
        Timber.e(throwable)
    }
}
