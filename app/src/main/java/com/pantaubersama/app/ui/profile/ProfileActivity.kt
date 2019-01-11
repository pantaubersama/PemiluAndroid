package com.pantaubersama.app.ui.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.* // ktlint-disable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.user.Badge
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.cluster.invite.UndangAnggotaActivity
import com.pantaubersama.app.ui.profile.cluster.requestcluster.RequestClusterActivity
import com.pantaubersama.app.ui.profile.setting.SettingActivity
import com.pantaubersama.app.ui.profile.linimasa.ProfileJanjiPolitikFragment
import com.pantaubersama.app.ui.profile.penpol.ProfileTanyaKandidatFragment
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.verifikasi.step1.Step1VerifikasiActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.State
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.badge_item_layout.view.*
import kotlinx.android.synthetic.main.cluster_options_layout.*
import kotlinx.android.synthetic.main.layout_leave_cluster_confirmation_dialog.*
import javax.inject.Inject

class ProfileActivity : BaseActivity<ProfilePresenter>(), ProfileView {

    @Inject
    override lateinit var presenter: ProfilePresenter

    private lateinit var activeFragment: Fragment
    private var pLinimasaFragment: ProfileJanjiPolitikFragment? = null
    private var pTanyaKandidatFragment: ProfileTanyaKandidatFragment? = null
    private var otherFrag: Fragment? = null // dummy

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        setupClusterLayout()
        setupBiodataLayout()
        setupBadgeLayout()
        initFragment()
        setupNavigation()
        presenter.refreshProfile()
        presenter.getProfile()
        presenter.refreshBadges()
    }

    override fun showProfile(profile: Profile) {
        iv_user_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_user_name.text = profile.name
        user_username.text = profile.username?.takeIf { it.isNotBlank() }?.let { "@%s".format(it) }
        user_bio.text = profile.about
        if (profile.verified) setVerified() else setUnverified()

        user_location.text = profile.location
        user_education.text = profile.education
        user_work.text = profile.occupation
        if (profile.cluster != null) parseCluster(profile.cluster!!)
    }

    private fun parseCluster(cluster: ClusterItem) {
        layout_cluster.visibility = View.VISIBLE
        tv_request_cluster.visibility = View.GONE
        cluster_image.loadUrl(cluster.image?.thumbnail?.url)
        cluster_name.text = cluster.name
        cluster_options_action.setOnClickListener {
            showClusterOptionsDialog(cluster)
        }
    }

    private fun setUnverified() {
        verified_icon.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(this@ProfileActivity, R.color.gray_dark_1), PorterDuff.Mode.MULTIPLY)
        verified_text.text = getString(R.string.txt_belum_verifikasi)
        verified_text.setTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.gray_dark_1))
        verified_button.setBackgroundResource(R.drawable.rounded_outline_gray)
        verified_button.setOnClickListener {
            val intent = Intent(this@ProfileActivity, Step1VerifikasiActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setVerified() {
        verified_icon.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(this@ProfileActivity, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        verified_text.text = getString(R.string.txt_terverifikasi)
        verified_text.setTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.colorAccent))
        verified_button.setBackgroundResource(R.drawable.rounded_outline_green)
    }

    private fun initFragment() {
        pLinimasaFragment = ProfileJanjiPolitikFragment.newInstance()
        pTanyaKandidatFragment = ProfileTanyaKandidatFragment.newInstance()
        otherFrag = Fragment()
    }

    private fun setupNavigation() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, pLinimasaFragment!!)
                .add(R.id.fragment_container, pTanyaKandidatFragment!!)
                .commit()

        activeFragment = pLinimasaFragment!!

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_menyerap -> run {
                    activeFragment = pLinimasaFragment!!
                    return@run
                }
                R.id.navigation_menggali -> run {
                    activeFragment = pTanyaKandidatFragment!!
                    return@run
                }
                R.id.navigation_menguji -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
                R.id.navigation_merayakan -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
                R.id.navigation_menjaga -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
            }

            showActiveFragment()

            return@OnNavigationItemSelectedListener true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_menyerap
    }

    private fun showActiveFragment() {
        supportFragmentManager.beginTransaction()
                .hide(pLinimasaFragment!!)
                .hide(pTanyaKandidatFragment!!)
                .show(activeFragment)
                .commit()
    }

    override fun showBadges(state: State, badges: List<Badge>) {
        badge_container.removeAllViews()
        progress_badge.visibleIf(state == State.Loading)
        tv_retry_badge.visibleIf(state is State.Error)
        tv_badge_more.visibleIf(state == State.Success)
        badges.forEach {
            val view = badge_container.inflate(R.layout.badge_item_layout).apply {
                iv_badge.loadUrl(it.image.thumbnail?.url, R.drawable.dummy_badge)
                iv_badge.setGrayScale(!it.achieved)
                badge_name.text = it.name
                badge_name.isEnabled = it.achieved
                badge_description.text = it.description
                badge_description.isEnabled = it.achieved
            }
            badge_container.addView(view)
        }
    }

    private fun setupBadgeLayout() {
        badge_expandable_button.setOnClickListener {
            badge_expandable.toggle()
            if (badge_expandable.isExpanded) {
                badge_expandable_image.animate().rotation(0F).start()
            } else {
                badge_expandable_image.animate().rotation(180F).start()
            }
        }
        tv_retry_badge.setOnClickListener { presenter.refreshBadges() }
        tv_badge_more.setOnClickListener {
            startActivity(Intent(this, BadgeActivity::class.java))
        }
    }

    private fun setupBiodataLayout() {
        biodata_expandable_button.setOnClickListener {
            biodata_expandable.toggle()
            if (biodata_expandable.isExpanded) {
                biodata_expandable_image.animate().rotation(0F).start()
            } else {
                biodata_expandable_image.animate().rotation(180F).start()
            }
        }
    }

    private fun setupClusterLayout() {
        cluster_expandable_button.setOnClickListener {
            cluster_expandable.toggle()
            if (cluster_expandable.isExpanded) {
                cluster_expandable_image.animate().rotation(0F).start()
            } else {
                cluster_expandable_image.animate().rotation(180F).start()
            }
        }
        tv_request_cluster.text = spannable {
            +"Belum ada Cluster "
            textColor(color(R.color.red)) {
                underline { +"( Request Cluster? )" }
            }
        }.toCharSequence()
        tv_request_cluster.setOnClickListener {
            startActivity(Intent(this, RequestClusterActivity::class.java))
        }
    }

    private fun showClusterOptionsDialog(cluster: ClusterItem) {
        val dialog = Dialog(this@ProfileActivity)
        dialog.setContentView(R.layout.cluster_options_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { _, i, _ ->
            if (i == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else {
                false
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.BOTTOM
        window?.attributes = lp
        dialog.invite_to_cluster_action?.setOnClickListener {
            val intent = Intent(this@ProfileActivity, UndangAnggotaActivity::class.java)
            intent.putExtra(PantauConstants.Cluster.CLUSTER_URL, cluster.magicLink)
            startActivity(intent)
        }
        dialog.leave_cluster_action?.setOnClickListener {
            showLeaveClusterConfirmationDialog(cluster)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLeaveClusterConfirmationDialog(cluster: ClusterItem) {
        val dialog = Dialog(this@ProfileActivity)
        dialog.setContentView(R.layout.layout_leave_cluster_confirmation_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else {
                false
            }
        }

        dialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        dialog.yes_button.setOnClickListener {
            presenter.leaveCluster(cluster.name)
            dialog.dismiss()
        }
        dialog.no_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showSuccessLeaveClusterAlert(name: String?) {
        ToastUtil.show(this@ProfileActivity, "Berhasil meninggalkan cluster $name")
    }

    override fun showRequestClusterLayout() {
        layout_cluster.visibility = View.GONE
        tv_request_cluster.visibility = View.VISIBLE
    }

    override fun showFailedLeaveClusterAlert(name: String?) {
        ToastUtil.show(this@ProfileActivity, "Gagal meninggalkan cluster $name")
    }

    override fun setLayout(): Int {
        return R.layout.activity_profile
    }

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings_action -> {
                val intent = Intent(this@ProfileActivity, SettingActivity::class.java)
                startActivityForResult(intent, PantauConstants.RequestCode.RC_SETTINGS)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.RequestCode.RC_SETTINGS) {
                presenter.refreshProfile()
            }
        }
    }
}