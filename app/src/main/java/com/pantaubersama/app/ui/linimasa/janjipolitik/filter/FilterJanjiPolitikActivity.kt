package com.pantaubersama.app.ui.linimasa.janjipolitik.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.clusterdialog.ClusterListDialog
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IS_SEARCH_FILTER
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_ALL
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_FALSE
import com.pantaubersama.app.utils.PantauConstants.Filter.USER_VERIFIED_TRUE
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_filter_janji_politik.*
import kotlinx.android.synthetic.main.item_cluster.*
import kotlinx.android.synthetic.main.item_default_dropdown_filter.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class FilterJanjiPolitikActivity : BaseActivity<FilterJanjiPolitikPresenter>(), FilterJanjiPolitikView {

    private var isSearchFilter = false

    @Inject override lateinit var presenter: FilterJanjiPolitikPresenter

    private var userFilter: String? = USER_VERIFIED_ALL
    private var clusterFilter: ClusterItem? = null

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_filter_janji_politik

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        fun setIntent(context: Context, isSearchFilter: Boolean): Intent {
            val intent = Intent(context, FilterJanjiPolitikActivity::class.java)
            intent.putExtra(EXTRA_IS_SEARCH_FILTER, isSearchFilter)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        isSearchFilter = intent.getBooleanExtra(EXTRA_IS_SEARCH_FILTER, false)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        tv_cluster_or_party_placeholder.text = getString(R.string.txt_semua_cluster)
        if (!isSearchFilter) {
            presenter.getFilter()
        } else {
            presenter.getSearchFilter()
        }

        rl_cluster_container.setOnClickListener {
            ClusterListDialog.show(supportFragmentManager, object : ClusterListDialog.DialogListener {
                override fun onClickItem(item: ClusterItem) {
                    selectCluster(item)
                }

                override fun onClickDefault() {
                    selectCluster(null)
                }
            })
        }

        radio_group_janpol.setOnCheckedChangeListener { _, checkId ->
            userFilter = when (checkId) {
                R.id.radbtn_semua -> USER_VERIFIED_ALL
                R.id.radbtn_belum_verifikasi -> USER_VERIFIED_FALSE
                R.id.radbtn_terverifikasi -> USER_VERIFIED_TRUE
                else -> USER_VERIFIED_ALL
            }
        }

        btn_terapkan.setOnClickListener {
            if (!isSearchFilter) {
                presenter.setFilter(this.userFilter!!, this.clusterFilter)
            } else {
                presenter.setSearchFilter(this.userFilter!!, this.clusterFilter)
            }
        }
    }

    private fun selectCluster(item: ClusterItem?) {
        this.clusterFilter = item
        if (item == null) {
            layout_default_cluster_filter.visibleIf(true)
            item_cluster.visibleIf(false)
        } else {
            layout_default_cluster_filter.visibleIf(false)
            item_cluster.visibleIf(true)
            tv_cluster_name.text = item.name
            tv_cluster_member_count.text = "${item.memberCount?.let { it } ?: "belum ada"} anggota"
            iv_avatar_cluster.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
        }
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showSelectedFilter(userFilter: String, clusterFilter: ClusterItem?) {
        this.userFilter = userFilter
        this.clusterFilter = clusterFilter

        selectCluster(clusterFilter)

        radio_group_janpol.check(when (userFilter) {
            USER_VERIFIED_TRUE -> R.id.radbtn_terverifikasi
            USER_VERIFIED_FALSE -> R.id.radbtn_belum_verifikasi
            else -> R.id.radbtn_semua
        })
    }

    override fun onSuccessSetFilter() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_reset -> {
                radio_group_janpol.check(R.id.radbtn_semua)
                selectCluster(null)
                ToastUtil.show(this, "Filter telah di reset")
                btn_terapkan.performClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
