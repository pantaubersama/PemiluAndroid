package com.pantaubersama.app.ui.note

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
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
    private lateinit var presidentAdapter: CarouselPagerAdapter
    private lateinit var partyAdapter: CarouselPagerAdapter
    private lateinit var profile: Profile

    private var selectedPaslon = 3
    private lateinit var selectedParty: PoliticalParty

    private var savedInstanceState: Bundle? = null

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
        this.savedInstanceState = savedInstanceState
        setupToolbar(true, getString(R.string.title_catatan_pilihanku), R.color.white, 4f)
        presenter.getUserProfile()
        presenter.getMyTendency()
        setupPresident()
        presenter.getPartai(1, 100)
    }

    override fun showPartiesProgressBar() {
        failed_alert.visibility = View.GONE
        parties_progressbar.visibility = View.VISIBLE
    }

    override fun dismissPartiesProgressBar() {
        parties_progressbar.visibility = View.GONE
    }

    override fun showFailedGetPartiesAlert() {
        failed_alert.visibility = View.VISIBLE
    }

    override fun showPartai(parties: MutableList<PoliticalParty>) {
        setupParty(parties)
        catatan_pilihanku_ok.setOnClickListener {
            presenter.submitCatatanku(selectedPaslon, selectedParty)
        }
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

            override fun setPosition(position: Int) {
                selectedParty = parties[position]
            }
        }
        party_viewpager.adapter = partyAdapter
        partyAdapter.notifyDataSetChanged()

        party_viewpager.addOnPageChangeListener(partyAdapter)
        if (savedInstanceState?.getInt("selected_party_position") != null) {
            savedInstanceState?.getInt("selected_party_position")?.let {
                selectedParty = parties[it]
                party_viewpager.currentItem = it
            }
        } else {
            if (profile.politicalParty != null) {
                for ((i, partai) in parties.withIndex()) {
                    if (partai == profile.politicalParty) {
                        party_viewpager.currentItem = i
                    }
                }
            } else {
                party_viewpager.currentItem = parties.size
            }
        }
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

            override fun setPosition(position: Int) {
                selectedPaslon = position + 1
            }
        }
        president_viewpager.adapter = presidentAdapter
        presidentAdapter.notifyDataSetChanged()

        president_viewpager.addOnPageChangeListener(presidentAdapter)

        if (savedInstanceState?.getInt("selected_paslon") != null) {
            savedInstanceState?.getInt("selected_paslon")?.let {
                selectedPaslon = it
                president_viewpager.currentItem = it - 1
            }
        } else {
            if (profile.votePreference != 0) {
                selectedPaslon = profile.votePreference
                president_viewpager.currentItem = profile.votePreference - 1
            } else {
                president_viewpager.currentItem = imageArray.size
            }
        }
        president_viewpager.offscreenPageLimit = 3
    }

    override fun bindUserProfile(profile: Profile) {
        this.profile = profile
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected_paslon", selectedPaslon)
        outState.putInt("selected_party_position", party_viewpager.currentItem)
    }
}
