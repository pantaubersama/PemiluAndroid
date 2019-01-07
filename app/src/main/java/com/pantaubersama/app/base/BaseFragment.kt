package com.pantaubersama.app.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.utils.extensions.snackBar
import timber.log.Timber

/**
 * @author edityomurti on 18/12/2018 01:03
 * This class should be extended only for classes that wants to be injected with Dagger.
 * Otherwise please use CommonFragment instead
 */
abstract class BaseFragment<P : BasePresenter<*>> : CommonFragment(), BaseView {
    protected var presenter: P? = null

    override fun onAttach(context: Context) {
        initInjection(createActivityComponent())
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenter()
        if (presenter != null) {
            presenter?.attach(this)
        }
    }

    protected abstract fun initInjection(activityComponent: ActivityComponent)

    protected abstract fun initPresenter(): P?

    override fun onDestroy() {
        if (presenter != null) {
            presenter?.detach()
        }
        super.onDestroy()
    }

    override fun showError(throwable: Throwable) {
        activity?.window?.decorView?.findViewById<View>(android.R.id.content)?.snackBar(throwable.message!!)
        Timber.e(throwable)
    }

    private fun createActivityComponent(): ActivityComponent {
        return (requireActivity().application as BaseApp).appComponent
                .withActivityComponent(ActivityModule(requireActivity()))
    }
}
