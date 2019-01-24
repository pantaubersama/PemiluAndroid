package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.ServiceModule
import com.pantaubersama.app.di.scope.ServiceScope
import dagger.Subcomponent

/**
 * Created by ali on 19/10/17.
 */
@ServiceScope
@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent {
    // injection
}