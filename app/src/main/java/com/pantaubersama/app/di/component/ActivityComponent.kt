package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.di.scope.ActivityScope
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.create.CreateJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.splashscreen.SplashScreenActivity
import dagger.Subcomponent

/**
 * Created by ali on 19/10/17.
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(profileActivity: ProfileActivity)
    fun inject(editProfileActivity: EditProfileActivity)
    fun inject(createTanyaKandidatActivity: CreateTanyaKandidatActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
    fun inject(tanyaKandidatFragment: TanyaKandidatFragment)
    fun inject(pilpresFragment: PilpresFragment)
    fun inject(filterPilpresActivity: FilterPilpresActivity)
    fun inject(janjiPolitikFragment: JanjiPolitikFragment)
    fun inject(createJanjiPolitikActivity: CreateJanjiPolitikActivity)
    fun inject(bannerInfoActivity: BannerInfoActivity)
    fun inject(kuisFragment: KuisFragment)
}