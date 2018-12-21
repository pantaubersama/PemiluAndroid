package com.pantaubersama.app.ui.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.* // ktlint-disable
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.cluster_options_layout.*

class ProfileActivity : BaseActivity<ProfilePresenter>(), ProfileView {

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
        setupClusterLayout()
        setupBiodataLayout()
        setupBadgeLayout()
        addBadgeItemLayout()
        cluster_options_action.setOnClickListener {
            showClusterOptionsDialog()
        }
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
        badge_expandable.collapse()
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
        biodata_expandable.collapse()
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
        cluster_expandable.collapse()
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
        dialog.setOnKeyListener { dialogInterface, i, keyEvent ->
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
                // open settings
            }
            R.id.open_cluster_action -> {
                // open cluster
            }
        }
        return super.onOptionsItemSelected(item)
    }
}