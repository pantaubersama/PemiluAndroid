package com.pantaubersama.app.ui.note

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_catatan_pilihan_revised.*
import javax.inject.Inject
import kotlin.math.roundToInt

class CatatanPilihanActivityRevised : BaseActivity<CatatanPilihanPresenter>(), CatatanPilihanView {
    lateinit var presidentAdapter: CarouselPagerAdapter
    lateinit var partyAdapter: CarouselPagerAdapter

    @Inject
    override lateinit var presenter: CatatanPilihanPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_catatan_pilihan_revised
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_catatan_pilihanku), R.color.white, 4f)
        presenter.getUserProfile()
        presenter.getMyTendency()
        setupPresident()
        presenter.getPartai(1, 100)
    }

    override fun showPartai(parties: MutableList<PoliticalParty>) {
        setupParty(parties)
    }

    private fun setupParty(parties: MutableList<PoliticalParty>) {
        // set page margin between pages for viewpager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val pageMargin = metrics.widthPixels / 3
        party_viewpager.pageMargin = -pageMargin

        partyAdapter = object : CarouselPagerAdapter(supportFragmentManager, party_viewpager, parties.size) {
            override fun getItemFragment(position: Int, scale: Float): Fragment {
                return CarouselPartyItemFragment.newInstance(parties[position], scale)
            }
        }
        party_viewpager.adapter = partyAdapter
        partyAdapter.notifyDataSetChanged()

        party_viewpager.addOnPageChangeListener(partyAdapter)
        party_viewpager.offscreenPageLimit = 7
    }

    override fun showLoading() {
        showProgressDialog("Menyimpan pilihan")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    private fun setupPresident() {
        // set page margin between pages for viewpager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val pageMargin = metrics.widthPixels / 3
        president_viewpager.pageMargin = -pageMargin

        val imageArray = intArrayOf(R.drawable.note_ava_paslon_1, R.drawable.note_ava_paslon_2, R.drawable.note_ava_paslon_3)

        presidentAdapter = object : CarouselPagerAdapter(supportFragmentManager, president_viewpager, imageArray.size) {
            override fun getItemFragment(position: Int, scale: Float): Fragment {
                return CarouselPresidentItemFragment.newInstance(imageArray[position], scale)
            }
        }
        president_viewpager.adapter = presidentAdapter
        presidentAdapter.notifyDataSetChanged()

        president_viewpager.addOnPageChangeListener(presidentAdapter)
        president_viewpager.offscreenPageLimit = 3
    }

    override fun bindUserProfile(profile: Profile) {
        if (profile.votePreference != 0) {
            profile.votePreference.let {
                president_viewpager.currentItem = it - 1
            }
        } else {
            president_viewpager.currentItem = 2
        }
    }

    override fun bindMyTendency(tendency: KuisUserResult, name: String) {
        ll_kuis_result.visibleIf(tendency.meta.finished != 0)
        presiden_total_kuis.text = spannable {
            +"Total Kecenderungan ${tendency.meta.finished} Dari ${tendency.meta.total} Kuis,\n"
            textColor(color(R.color.black_3)) { +name }
            +" lebih suka jawaban dari Paslon no ${tendency.team.id}"
        }.toCharSequence()
        paslon_avatar.loadUrl(tendency.team.avatar)
        total_tendency.text = "%d%%".format(tendency.percentage.roundToInt())
        paslon_name.text = tendency.team.title
    }

    override fun showFailedGetMyTendencyAlert() {
        ToastUtil.show(this@CatatanPilihanActivityRevised, "Gagal memuat kecenderungan")
    }

    override fun showSuccessSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivityRevised, "Pilihan tersimpan")
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showFailedSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivityRevised, "Gagal menyimpan pilihan")
    }
}
