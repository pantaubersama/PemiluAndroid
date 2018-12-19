package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.di.scope.ActivityScope
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.splashscreen.SplashScreenActivity
import dagger.Subcomponent

/**
 * Created by ali on 19/10/17.
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(createTanyaKandidatActivity: CreateTanyaKandidatActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
}