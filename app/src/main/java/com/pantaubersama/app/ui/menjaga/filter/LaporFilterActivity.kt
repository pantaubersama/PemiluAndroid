package com.pantaubersama.app.ui.menjaga.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.menjaga.filter.partiesdialog.PartiesDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_lapor_filter.*
import kotlinx.android.synthetic.main.item_default_dropdown_filter.*
import kotlinx.android.synthetic.main.item_partai_pilihan.*
import kotlinx.android.synthetic.main.layout_button_terapkan_filter.*
import javax.inject.Inject

class LaporFilterActivity : BaseActivity<LaporFilterPresenter>(), LaporFilterView {
    private var isSearchFilter = false
    private var userFilter: String? = null
    private var partyFilter: PoliticalParty? = null
    @Inject
    override lateinit var presenter: LaporFilterPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_lapor_filter
    }

    override fun fetchIntentExtra() {
        isSearchFilter = intent.getBooleanExtra(PantauConstants.Extra.EXTRA_IS_SEARCH_FILTER, false)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        tv_cluster_or_party_placeholder.text = getString(R.string.txt_semua_partai)
        if (!isSearchFilter) {
            presenter.getFilter()
        } else {
            presenter.getSearchFilter()
        }

        rl_party_container.setOnClickListener {
            PartiesDialog.show(supportFragmentManager, object : PartiesDialog.DialogListener {
                override fun onClickItem(item: PoliticalParty) {
                    setParty(item)
                }

                override fun onClickDefault() {
                    setParty(null)
                }
            })
        }

        radio_group_lapor.setOnCheckedChangeListener { _, checkId ->
            userFilter = when (checkId) {
                R.id.radbtn_semua -> PantauConstants.Filter.USER_VERIFIED_ALL
                R.id.radbtn_belum_verifikasi -> PantauConstants.Filter.USER_VERIFIED_FALSE
                R.id.radbtn_terverifikasi -> PantauConstants.Filter.USER_VERIFIED_TRUE
                else -> PantauConstants.Filter.USER_VERIFIED_ALL
            }
        }

        btn_terapkan.setOnClickListener {
//            if (!isSearchFilter) {
//                presenter.setFilter(userFilter, this.clusterFilter)
//            } else {
//                presenter.setSearchFilter(userFilter, this.clusterFilter)
//            }
        }
    }

    private fun setParty(item: PoliticalParty?) {
        this.partyFilter = item
        if (item == null) {
            layout_default_dropdown_filter.visibleIf(true)
            item_cluster.visibleIf(false)
        } else {
            layout_default_dropdown_filter.visibleIf(false)
            item_cluster.visibleIf(true)
            partai_pilihan_nama.text = item.name
            if (item.name != getString(R.string.txt_semua_partai)) {
                partai_pilihan_foto_partai.visibility = View.VISIBLE
                partai_pilihan_no_urut.visibility = View.VISIBLE
                partai_pilihan_foto_partai.loadUrl(item.image?.url, R.drawable.ic_avatar_placeholder)
                partai_pilihan_no_urut.text = "Nomor urut ${item.number}"
            } else {
                partai_pilihan_foto_partai.visibility = View.GONE
                partai_pilihan_no_urut.visibility = View.GONE
            }
        }
    }

    override fun bindLaporUserFilter(loadLaporUserFilter: String) {
        radio_group_lapor.check(
            when (userFilter) {
                PantauConstants.Filter.USER_VERIFIED_TRUE -> R.id.radbtn_terverifikasi
                PantauConstants.Filter.USER_VERIFIED_FALSE -> R.id.radbtn_belum_verifikasi
                else -> R.id.radbtn_semua
            }
        )
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun start(activity: Activity, requestCode: Int, isSearchFilter: Boolean? = null) {
            val intent = Intent(activity, LaporFilterActivity::class.java)
            if (isSearchFilter != null) {
                intent.putExtra(PantauConstants.Extra.EXTRA_IS_SEARCH_FILTER, isSearchFilter)
            } else {
                intent.putExtra(PantauConstants.Extra.EXTRA_IS_SEARCH_FILTER, false)
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
