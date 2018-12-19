package com.pantaubersama.app.base

import android.app.ProgressDialog
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pantaubersama.app.R
import java.util.Objects

/**
 * @author edityomurti on 18/12/2018 01:03
 */
abstract class BaseFragment<P : BasePresenter<*>> : Fragment(), BaseView {
    protected var progressDialog: ProgressDialog? = null
    protected var presenter: P? = null
    protected var toolbar: Toolbar? = null
    private var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(setLayout(), container, false)
        initProgressDialog()
        presenter = initPresenter()
        initView(mView!!)
        return mView
    }

    protected abstract fun initPresenter(): P?

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (presenter != null) {
            presenter?.attach(this)
        }
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter?.detach()
        }
        super.onDestroy()
    }
}