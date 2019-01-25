package com.pantaubersama.app.base

import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.module.ActivityModule

/**
 * @author edityomurti on 14/12/2018 17:33
 * This class should be extended only for activities that has presenter.
 * Otherwise please use CommonActivity instead
 */
abstract class BaseActivity<P : BasePresenter<*>> : CommonActivity(), BaseView {

    protected abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection(createActivityComponent())
        presenter.attach(this)
        super.onCreate(savedInstanceState)
        // prevent all crash
//        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
//            Log.e("Error" + Thread.currentThread().stackTrace[2], paramThrowable.localizedMessage)
//        }
    }

    // TODO: make this abstract instead of open
    protected open fun initInjection(activityComponent: ActivityComponent) {}

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    private fun createActivityComponent(): ActivityComponent {
        return (application as BaseApp).appComponent
                .withActivityComponent(ActivityModule(this))
    }
}