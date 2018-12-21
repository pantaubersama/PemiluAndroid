package com.pantaubersama.app.ui.profile

import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*

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