package com.pantaubersama.app.base.viewholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.base.BaseView

/**
 * @author edityomurti on 27/12/2018 01:03
 */
abstract class BaseDialogFragment<P : BasePresenter<*>> : DialogFragment(), BaseView {

    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjection()
        presenter = initPresenter()
        if (presenter != null) {
            presenter?.attach(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    protected abstract fun initPresenter(): P?

    protected open fun initInjection() {}

    protected abstract fun initView(view: View)

    @LayoutRes
    protected abstract fun setLayout(): Int

    override fun onDestroy() {
        if (presenter != null) {
            presenter?.detach()
        }
        super.onDestroy()
    }

}