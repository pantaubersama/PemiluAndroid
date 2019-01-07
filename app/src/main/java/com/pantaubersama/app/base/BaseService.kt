package com.pantaubersama.app.base

import android.app.Service
import com.pantaubersama.app.di.component.ServiceComponent
import com.pantaubersama.app.di.module.ServiceModule

abstract class BaseService : Service() {

    fun createServiceComponent(): ServiceComponent {
        return (application as BaseApp).appComponent
                .withServiceComponent(ServiceModule(this))
    }
}