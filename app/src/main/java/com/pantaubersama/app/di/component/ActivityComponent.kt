package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.di.scope.ActivityScope
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.clusterdialog.ClusterListDialog
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.create.CreateJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.note.CatatanPilihanActivity
import com.pantaubersama.app.ui.note.presiden.PresidenFragment
import com.pantaubersama.app.ui.penpol.kuis.detail.DetailKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.filter.FilterKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.kuisstart.KuisActivity
import com.pantaubersama.app.ui.penpol.kuis.list.KuisFragment
import com.pantaubersama.app.ui.penpol.kuis.result.KuisResultActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisSummaryActivity
import com.pantaubersama.app.ui.penpol.kuis.result.KuisUserResultActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.filter.FilterTanyaKandidatActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatFragment
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.profile.cluster.categery.ClusterCategoryActivity
import com.pantaubersama.app.ui.profile.cluster.invite.UndangAnggotaActivity
import com.pantaubersama.app.ui.profile.cluster.requestcluster.RequestClusterActivity
import com.pantaubersama.app.ui.profile.connect.ConnectActivity
import com.pantaubersama.app.ui.profile.linimasa.ProfileJanjiPolitikFragment
import com.pantaubersama.app.ui.profile.penpol.ProfileTanyaKandidatFragment
import com.pantaubersama.app.ui.profile.setting.SettingActivity
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.panduankomunitas.PanduanKomunitasActivity
import com.pantaubersama.app.ui.profile.setting.tentangapp.TentangAppActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.ui.profile.setting.ubahsandi.UbahSandiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step5.Step5VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step7.Step7VerifikasiActivity
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
    fun inject(filterTanyaKandidatActivity: FilterTanyaKandidatActivity)
    fun inject(janjiPolitikFragment: JanjiPolitikFragment)
    fun inject(createJanjiPolitikActivity: CreateJanjiPolitikActivity)
    fun inject(bannerInfoActivity: BannerInfoActivity)
    fun inject(kuisFragment: KuisFragment)
    fun inject(settingActivity: SettingActivity)
    fun inject(badgeActivity: BadgeActivity)
    fun inject(ubahSandiActivity: UbahSandiActivity)
    fun inject(ubahDataLaporActivity: UbahDataLaporActivity)
    fun inject(filterKuisActivity: FilterKuisActivity)
    fun inject(filterJanjiPolitikActivity: FilterJanjiPolitikActivity)
    fun inject(clusterListDialog: ClusterListDialog)
    fun inject(step1VerifikasiActivity: Step1VerifikasiActivity)
    fun inject(step3VerifikasiActivity: Step3VerifikasiActivity)
    fun inject(step5VerifikasiActivity: Step5VerifikasiActivity)
    fun inject(step7VerifikasiActivity: Step7VerifikasiActivity)
    fun inject(clusterCategoryActivity: ClusterCategoryActivity)
    fun inject(detailJanjiPolitikActivity: DetailJanjiPolitikActivity)
    fun inject(requestClusterActivity: RequestClusterActivity)
    fun inject(kuisActivity: KuisActivity)
    fun inject(kuisResultActivity: KuisResultActivity)
    fun inject(kuisSummaryActivity: KuisSummaryActivity)
    fun inject(undangAnggotaActivity: UndangAnggotaActivity)
    fun inject(kuisUserResultActivity: KuisUserResultActivity)
    fun inject(connectActivity: ConnectActivity)
    fun inject(tentangAppActivity: TentangAppActivity)
    fun inject(panduanKomunitasActivity: PanduanKomunitasActivity)
    fun inject(profileJanjiPolitikFragment: ProfileJanjiPolitikFragment)
    fun inject(profileTanyaKandidatFragment: ProfileTanyaKandidatFragment)
    fun inject(presidenFragment: PresidenFragment)
    fun inject(catatanPilihanActivity: CatatanPilihanActivity)
    fun inject(detailTanyaKandidatActivity: DetailTanyaKandidatActivity)
    fun inject(detailKuisActivity: DetailKuisActivity)
    fun inject(detailBadgeActivity: DetailBadgeActivity)
}