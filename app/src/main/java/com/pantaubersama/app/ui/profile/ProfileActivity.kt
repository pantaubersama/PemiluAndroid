package com.pantaubersama.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import com.pantaubersama.app.data.model.user.VerificationStep
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.clusterdetail.ClusterDetailActivity
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.profile.cluster.invite.UndangAnggotaActivity
import com.pantaubersama.app.ui.profile.cluster.requestcluster.RequestClusterActivity
import com.pantaubersama.app.ui.profile.setting.SettingActivity
import com.pantaubersama.app.ui.profile.linimasa.ProfileJanjiPolitikFragment
import com.pantaubersama.app.ui.profile.penpol.ProfileTanyaKandidatFragment
import com.pantaubersama.app.ui.profile.setting.badge.BadgeActivity
import com.pantaubersama.app.ui.profile.verifikasi.VerifikasiNavigator
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.State
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.badge_item_layout.view.*
import java.lang.IllegalStateException
import javax.inject.Inject

class ProfileActivity : BaseActivity<ProfilePresenter>(), ProfileView {
    @Inject
    override lateinit var presenter: ProfilePresenter
    private var userId: String? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? = 0
    override fun setLayout(): Int = R.layout.activity_profile

    override fun fetchIntentExtra() {
        userId = intent.getStringExtra(PantauConstants.Profile.USER_ID)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "", R.color.white, 4f)
        setupClusterLayout()
        setupBiodataLayout()
        setupBadgeLayout()
        if (savedInstanceState == null) {
            showFragment(ProfileJanjiPolitikFragment.newInstance(userId), ProfileJanjiPolitikFragment.TAG)
        }
        setupNavigation()
        if (userId == null) {
            presenter.refreshProfile()
            presenter.getProfile()
            presenter.refreshBadges()
        } else {
            userId?.let { presenter.getUserProfile(it) }
            userId?.let { presenter.getUserBadge(it) }
        }
    }

    override fun showProfile(profile: Profile) {
        iv_user_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_user_name.text = profile.fullName
        user_username.text = profile.username?.takeIf { it.isNotBlank() }?.let { "@%s".format(it) }
        user_bio.text = profile.about
        if (profile.verified == true) setVerified() else setUnverified()
        empty_data_alert.visibleIf(profile.occupation == null && profile.education == null && profile.location == null)
        if (profile.occupation != null) user_location.text = profile.location else location_container.visibility = View.GONE
        if (profile.education != null) user_education.text = profile.education else education_container.visibility = View.GONE
        if (profile.occupation != null) user_work.text = profile.occupation else work_container.visibility = View.GONE
        if (profile.cluster != null) parseCluster(profile.cluster!!)
    }

    private fun parseCluster(cluster: ClusterItem) {
        layout_cluster.visibility = View.VISIBLE
        layout_cluster.setOnClickListener {
            cluster.id?.let { it1 -> ClusterDetailActivity.start(this@ProfileActivity, it1) }
        }
        tv_request_cluster.visibility = View.GONE
        cluster_image.loadUrl(cluster.image?.thumbnail?.url)
        cluster_name.text = cluster.name
        if (userId == null) {
            cluster_options_action.setOnClickListener {
                showClusterOptionsDialog(cluster)
            }
        } else {
            cluster_options_action.visibility = View.GONE
        }
    }

    private fun setUnverified() {
        verified_icon.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(this@ProfileActivity, R.color.gray_dark_1), PorterDuff.Mode.MULTIPLY)
        verified_text.text = getString(R.string.txt_belum_verifikasi)
        verified_text.setTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.gray_dark_1))
        verified_button.setBackgroundResource(R.drawable.rounded_outline_gray)
        verified_button.setOnClickListener {
            presenter.getStatusVerifikasi()
        }
    }

    private fun setVerified() {
        verified_icon.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(this@ProfileActivity, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        verified_text.text = getString(R.string.txt_terverifikasi)
        verified_text.setTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.colorAccent))
        verified_button.setBackgroundResource(R.drawable.rounded_outline_green)
        verified_button.setOnClickListener(null)
    }

    private fun setupNavigation() {
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val (fragment, tag) = when (item.itemId) {
                R.id.navigation_menyerap -> ProfileJanjiPolitikFragment.newInstance(userId) to ProfileJanjiPolitikFragment.TAG
                R.id.navigation_menggali -> ProfileTanyaKandidatFragment.newInstance(userId) to ProfileTanyaKandidatFragment.TAG
//                R.id.navigation_menguji -> WordStadiumFragment.newInstance() to WordStadiumFragment.TAG
//                R.id.navigation_menjaga -> WordStadiumFragment.newInstance() to WordStadiumFragment.TAG
//                R.id.navigation_merayakan -> MerayakanFragment.newInstance() to MerayakanFragment.TAG
                else -> throw IllegalStateException("unknown menu")
            }
            showFragment(fragment, tag)
//            nested_scroll.scrollTo(0, fragment_container.top + 30)
            true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showFragment(fragment: Fragment, tag: String) = with(supportFragmentManager) {
        val transaction = beginTransaction()
        var nextFragment = findFragmentByTag(tag)

        primaryNavigationFragment?.let { transaction.hide(it) }
        if (nextFragment != null) {
            transaction.show(nextFragment)
        } else {
            nextFragment = fragment
            transaction.add(R.id.fragment_container, nextFragment, tag)
        }
        transaction.setPrimaryNavigationFragment(nextFragment)
        transaction.setReorderingAllowed(true)
        transaction.commit()
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
            if (userId != null) {
                BadgeActivity.start(this@ProfileActivity, userId)
            } else {
                BadgeActivity.start(this@ProfileActivity)
            }
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
        if (userId == null) {
            tv_request_cluster.text = spannable {
                +"Belum ada Cluster "
                textColor(color(R.color.red)) {
                    underline { +"( Request Cluster? )" }
                }
            }.toCharSequence()
            tv_request_cluster.setOnClickListener {
                startActivity(Intent(this, RequestClusterActivity::class.java))
            }
        } else {
            tv_request_cluster.text = "Belum ada cluster"
        }
    }

    private fun showClusterOptionsDialog(cluster: ClusterItem) {
        val dialog = OptionDialog(this, R.layout.cluster_options_layout)
        dialog.show()
        dialog.listener = object : OptionDialog.DialogListener {
            override fun onClick(viewId: Int) {
                when (viewId) {
                    R.id.invite_to_cluster_action -> {
//                        val intent = Intent(this@ProfileActivity, UndangAnggotaActivity::class.java)
//                        intent.putExtra(PantauConstants.Cluster.CLUSTER_URL, cluster.magicLink)
//                        intent.putExtra(PantauConstants.Cluster.CLUSTER_ID, cluster.id)
//                        intent.putExtra(PantauConstants.Cluster.INVITE_LINK_ACTIVE, cluster.isLinkActive)
//                        intent.putExtra(EXTRA_IS_MODERATOR, presenter.getMyProfile().isModerator)

                        startActivityForResult(UndangAnggotaActivity.setIntent(this@ProfileActivity, cluster.magicLink, cluster.id, cluster.isLinkActive, presenter.getMyProfile().isModerator == true), PantauConstants.Cluster.REQUEST_CODE.REQUEST_CLUSTER)
                        dialog.dismiss()
                    }
                    R.id.leave_cluster_action -> {
                        showLeaveClusterConfirmationDialog(cluster)
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun showLeaveClusterConfirmationDialog(cluster: ClusterItem) {
        ConfirmationDialog.Builder()
            .with(this@ProfileActivity)
            .setDialogTitle("Tinggalkan Cluster?")
            .setAlert("Apakah Anda yakin akan meninggalkan cluster ini?")
            .setCancelText("Batal")
            .setOkText("Ya, Tinggalkan")
            .addOkListener(object : ConfirmationDialog.DialogOkListener {
                override fun onClickOk() {
                    presenter.leaveCluster(cluster.name)
                }
            })
            .show()
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

    override fun showLoading() {
        progress_bar.visibleIf(true)
    }

    override fun dismissLoading() {
        progress_bar.visibleIf(false)
    }

    override fun showFailedGetProfileAlert() {
        ToastUtil.show(this@ProfileActivity, "Gagal memuat profil pengguna")
    }

    override fun showVerifikasiScreen(step: VerificationStep) {
        VerifikasiNavigator.start(this, step)
    }

    override fun showFailedGetVerifikasi() {
        ToastUtil.show(this, "Gagal mendapatkan status verifikasi")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (userId == null) {
            menuInflater.inflate(R.menu.menu_profile, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings_action -> {
                val intent = Intent(this@ProfileActivity, SettingActivity::class.java)
                startActivityForResult(intent, PantauConstants.RequestCode.RC_SETTINGS)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.RequestCode.RC_SETTINGS) {
                presenter.refreshProfile()
            } else if (requestCode == PantauConstants.Cluster.REQUEST_CODE.REQUEST_CLUSTER) {
                presenter.refreshProfile()
            }
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        super.onBackPressed()
    }
}