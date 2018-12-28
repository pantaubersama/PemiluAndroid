package com.pantaubersama.app.ui.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.* // ktlint-disable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Avatar
import com.pantaubersama.app.data.model.user.User
import com.pantaubersama.app.ui.profile.setting.SettingActivity
import com.pantaubersama.app.ui.profile.linimasa.ProfileJanjiPolitikFragment
import com.pantaubersama.app.ui.profile.penpol.ProfileTanyaKandidatFragment
import com.pantaubersama.app.ui.profile.verifikasi.Step1VerifikasiActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.cluster_options_layout.*

class ProfileActivity : BaseActivity<ProfilePresenter>(), ProfileView {
    private lateinit var activeFragment: Fragment
    private var pLinimasaFragment: ProfileJanjiPolitikFragment? = null
    private var pTanyaKandidatFragment: ProfileTanyaKandidatFragment? = null
    private var otherFrag: Fragment? = null // dummy

    override fun initPresenter(): ProfilePresenter? {
        return ProfilePresenter()
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        setupToolbar(true, "", R.color.white, 4f)
        setProfileData()
        setupClusterLayout()
        setupBiodataLayout()
        setupBadgeLayout()
        addBadgeItemLayout()
        cluster_options_action.setOnClickListener {
            showClusterOptionsDialog()
        }
        initFragment()
        setupNavigation()
    }

    private fun setProfileData() {
        val user = User(
            "8787",
            "haryonosugi@gmail.com",
            "Haryono",
            "Sugi",
                "haryono",
                Avatar(),
                false)
        if (user.verified!!) {
            setVerified()
        } else {
            setUnverified()
        }
    }

    private fun setUnverified() {
        verified_icon.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(this@ProfileActivity, R.color.gray_dark_1), PorterDuff.Mode.MULTIPLY)
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
                R.id.navigation_linimasa -> run {
                    activeFragment = pLinimasaFragment!!
                    return@run
                }
                R.id.navigation_penpol -> run {
                    activeFragment = pTanyaKandidatFragment!!
                    return@run
                }
                R.id.navigation_wordstadium -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
                R.id.navigation_lapor -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
                R.id.navigation_rekap -> run {
                    activeFragment = otherFrag!!
                    return@run
                }
            }

            showActiveFragment()

            return@OnNavigationItemSelectedListener true
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_linimasa
    }

    private fun showActiveFragment() {
        supportFragmentManager.beginTransaction()
                .hide(pLinimasaFragment!!)
                .hide(pTanyaKandidatFragment!!)
                .show(activeFragment)
                .commit()
    }

    private fun addBadgeItemLayout() {
        badge_container.removeAllViews()
        val inflater = LayoutInflater.from(this@ProfileActivity)
        for (i in 1..3) {
            val view = inflater.inflate(R.layout.badge_item_layout, null, false)
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
    }

    private fun showClusterOptionsDialog() {
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
            // invite
        }
        dialog.leave_cluster_action?.setOnClickListener {
            // leave
        }
        dialog.show()
    }

    override fun setLayout(): Int {
        return R.layout.activity_profile
    }

    override fun showLoading() {
        // show loading
    }

    override fun dismissLoading() {
        // hide loading
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings_action -> {
                val intent = Intent(this@ProfileActivity, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.open_cluster_action -> {
                // open cluster
            }
        }
        return super.onOptionsItemSelected(item)
    }
}