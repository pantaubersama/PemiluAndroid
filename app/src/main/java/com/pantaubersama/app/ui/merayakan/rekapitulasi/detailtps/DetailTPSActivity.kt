package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_detail_tps.*
import javax.inject.Inject

class DetailTPSActivity : BaseActivity<DetailTPSPresenter>(), DetailTPSView {

    @Inject
    override lateinit var presenter: DetailTPSPresenter

    private var tps: TPS? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        tps = intent.getSerializableExtra(TPS_DATA) as TPS
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_detail_tps
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "TPS", R.color.white, 4f)
        bindTpsData()
        loadData()
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun bindTpsData() {
        tps?.let {
            user_avatar.loadUrl(it.user.avatar?.url, R.drawable.ic_avatar_placeholder)
            user_name.text = it.user.fullName
            user_cluster.text = it.user.cluster?.name
            tps_number.text = "TPS ${it.tps}"
            tps_address.text = "${it.province.name}, ${it.regency.name}, ${it.district.name}, ${it.village.name}"
        }
    }

    private fun loadData() {
        swipe_refresh.isRefreshing = false
        tps?.id?.let {
            tps?.village?.code?.let { it1 -> tps?.tps?.let { it2 -> presenter.getRekapitulasi(it, it1, it2) } }
        }
    }

    override fun showFailedGetRealCountAlert() {
        failed_alert.visibility = View.VISIBLE
    }

    override fun bindRealCount(rekapitulasi: Percentage) {
        paslon_1_percentage.text = String.format("%.2f", rekapitulasi.candidates?.get(0)?.percentage)
        paslon_1_votes_count.text = "${rekapitulasi.candidates?.get(0)?.totalVote} suara"
        paslon_2_percentage.text = String.format("%.2f", rekapitulasi.candidates?.get(1)?.percentage)
        paslon_2_votes_count.text = "${rekapitulasi.candidates?.get(1)?.totalVote} suara"
        invalid_vite_count.text = rekapitulasi.invalidVote?.total.toString()
        votes_count.text = rekapitulasi.totalVote.toString()
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.close_action -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, item: TPS, requestCode: Int? = null) {
            val intent = Intent(context, DetailTPSActivity::class.java)
            intent.putExtra(TPS_DATA, item)
            context.startActivity(intent)
        }
    }
}
