package com.pantaubersama.app.di.component

import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.di.scope.ActivityScope
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.ui.categorydialog.CategoryListDialog
import com.pantaubersama.app.ui.clusterdetail.ClusterDetailActivity
import com.pantaubersama.app.ui.clusterdialog.ClusterListDialog
import com.pantaubersama.app.ui.debat.DebatActivity
import com.pantaubersama.app.ui.debat.detail.DetailDebatActivity
import com.pantaubersama.app.ui.debat.detail.DetailDebatDialogFragment
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.JanjiPolitikFragment
import com.pantaubersama.app.ui.linimasa.janjipolitik.create.CreateJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.janjipolitik.filter.FilterJanjiPolitikActivity
import com.pantaubersama.app.ui.linimasa.pilpres.filter.FilterPilpresActivity
import com.pantaubersama.app.ui.linimasa.pilpres.PilpresFragment
import com.pantaubersama.app.ui.login.LoginActivity
import com.pantaubersama.app.ui.menguji.home.MengujiPagerFragment
import com.pantaubersama.app.ui.menguji.list.DebatListActivity
import com.pantaubersama.app.ui.menjaga.filter.LaporFilterActivity
import com.pantaubersama.app.ui.menjaga.filter.partiesdialog.PartiesDialog
import com.pantaubersama.app.ui.menjaga.lapor.LaporFragment
import com.pantaubersama.app.ui.merayakan.perhitungan.create.c1.C1FormActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome.PerhitunganMainActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd.PerhitunganDPDActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpr.PerhitunganDPRActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata.DataTPSActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen.UploadDocumentActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.list.PerhitunganFragment
import com.pantaubersama.app.ui.merayakan.rekapitulasi.home.RekapitulasiFragment
import com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah.RekapitulasiDaerahActivity
import com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps.DetailTPSActivity
import com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist.TPSListActivity
import com.pantaubersama.app.ui.note.CatatanPilihanActivityRevised
import com.pantaubersama.app.ui.notification.NotifActivity
import com.pantaubersama.app.ui.onboarding.OnboardingActivity
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
import com.pantaubersama.app.ui.profile.cluster.category.ClusterCategoryActivity
import com.pantaubersama.app.ui.profile.cluster.invite.UndangAnggotaActivity
import com.pantaubersama.app.ui.profile.cluster.requestcluster.RequestClusterActivity
import com.pantaubersama.app.ui.profile.connect.ConnectActivity
import com.pantaubersama.app.ui.profile.linimasa.ProfileJanjiPolitikFragment
import com.pantaubersama.app.ui.profile.penpol.ProfileTanyaKandidatFragment
import com.pantaubersama.app.ui.profile.setting.SettingActivity
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.ui.profile.setting.clusterundang.ClusterUndangActivity
import com.pantaubersama.app.ui.profile.setting.editprofile.EditProfileActivity
import com.pantaubersama.app.ui.profile.setting.panduankomunitas.PanduanKomunitasActivity
import com.pantaubersama.app.ui.profile.setting.tentangapp.TentangAppActivity
import com.pantaubersama.app.ui.profile.setting.ubahdatalapor.UbahDataLaporActivity
import com.pantaubersama.app.ui.profile.verifikasi.step0.Step0VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step2.Step2VerifikasiActivity
import com.pantaubersama.app.ui.profile.verifikasi.step3.Step3VerifikasiActivity
import com.pantaubersama.app.ui.search.SearchActivity
import com.pantaubersama.app.ui.search.cluster.SearchClusterFragment
import com.pantaubersama.app.ui.search.cluster.filter.FilterClusterActivity
import com.pantaubersama.app.ui.search.history.SearchHistoryFragment
import com.pantaubersama.app.ui.search.janjipolitik.SearchJanjiPolitikFragment
import com.pantaubersama.app.ui.search.linimasa.SearchLinimasaFragment
import com.pantaubersama.app.ui.search.person.SearchPersonFragment
import com.pantaubersama.app.ui.search.person.filter.FilterOrangActivity
import com.pantaubersama.app.ui.search.tanya.SearchQuestionFragment
import com.pantaubersama.app.ui.search.quiz.SearchQuizFragment
import com.pantaubersama.app.ui.splashscreen.SplashScreenActivity
import com.pantaubersama.app.ui.wordstadium.challenge.direct.DirectChallengeActivity
import com.pantaubersama.app.ui.wordstadium.challenge.direct.PreviewChallengeActivity
import com.pantaubersama.app.ui.bidangkajiandialog.BidangKajianDialog
import com.pantaubersama.app.ui.notification.NotifChildFragment
import com.pantaubersama.app.ui.wordstadium.challenge.open.OpenChallengeActivity
import com.pantaubersama.app.ui.wordstadium.challenge.open.PromoteChallengeActivity
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
    fun inject(ubahDataLaporActivity: UbahDataLaporActivity)
    fun inject(filterKuisActivity: FilterKuisActivity)
    fun inject(filterJanjiPolitikActivity: FilterJanjiPolitikActivity)
    fun inject(clusterListDialog: ClusterListDialog)
    fun inject(step0VerifikasiActivity: Step0VerifikasiActivity)
    fun inject(step1VerifikasiActivity: Step1VerifikasiActivity)
    fun inject(step2VerifikasiActivity: Step2VerifikasiActivity)
    fun inject(step3VerifikasiActivity: Step3VerifikasiActivity)
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
    fun inject(detailTanyaKandidatActivity: DetailTanyaKandidatActivity)
    fun inject(detailKuisActivity: DetailKuisActivity)
    fun inject(detailBadgeActivity: DetailBadgeActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(searchHistoryFragment: SearchHistoryFragment)
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(searchLinimasaFragment: SearchLinimasaFragment)
    fun inject(searchJanjiPolitikFragment: SearchJanjiPolitikFragment)
    fun inject(searchClusterFragment: SearchClusterFragment)
    fun inject(filterClusterActivity: FilterClusterActivity)
    fun inject(categoryListDialog: CategoryListDialog)
    fun inject(searchPersonFragment: SearchPersonFragment)
    fun inject(clusterUndangActivity: ClusterUndangActivity)
    fun inject(searchQuestionFragment: SearchQuestionFragment)
    fun inject(searchQuizFragment: SearchQuizFragment)
    fun inject(filterOrangActivity: FilterOrangActivity)
    fun inject(clusterDetailActivity: ClusterDetailActivity)
    fun inject(laporFragment: LaporFragment)
    fun inject(laporFilterActivity: LaporFilterActivity)
    fun inject(partiesDialog: PartiesDialog)
    fun inject(notifActivity: NotifActivity)
    fun inject(mengujiPagerFragment: MengujiPagerFragment)
    fun inject(perhitunganFragment: PerhitunganFragment)
    fun inject(perhitunganDPRRIActivity: PerhitunganDPRActivity)
    fun inject(notifChildFragment: NotifChildFragment)
    fun inject(perhitunganDPDActivity: PerhitunganDPDActivity)
    fun inject(openChallengeActivity: OpenChallengeActivity)
    fun inject(kajianDialog: BidangKajianDialog)
    fun inject(debatActivity: DebatActivity)
    fun inject(rekapitulasiFragment: RekapitulasiFragment)
    fun inject(rekapitulasiProvinsiActivity: RekapitulasiDaerahActivity)
    fun inject(tpsListActivity: TPSListActivity)
    fun inject(detailTPSActivity: DetailTPSActivity)
    fun inject(uploadDocumentActivity: UploadDocumentActivity)
    fun inject(perhitunganMainActivity: PerhitunganMainActivity)
    fun inject(promoteChallengeActivity: PromoteChallengeActivity)
    fun inject(debatListActivity: DebatListActivity)
    fun inject(perhitunganPresidenActivity: PerhitunganPresidenActivity)
    fun inject(dataTPSActivity: DataTPSActivity)
    fun inject(detailDebatActivity: DetailDebatActivity)
    fun inject(directChallengeActivity: DirectChallengeActivity)
    fun inject(previewChallengeActivity: PreviewChallengeActivity)
    fun inject(detailDebatDialogFragment: DetailDebatDialogFragment)
    fun inject(c1FormActivity: C1FormActivity)
    fun inject(catatanPilihanActivityRevised: CatatanPilihanActivityRevised)
}