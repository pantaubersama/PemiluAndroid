package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.Dapil
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRPartaiAdapter
import com.pantaubersama.app.utils.PantauConstants.Merayakan.REAL_COUNT_TYPE
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import kotlinx.android.synthetic.main.activity_perhitungan_dpr.*
import javax.inject.Inject

class PerhitunganDPRActivity : BaseActivity<PerhitunganDPRPresenter>(), PerhitunganDPRView {
    private lateinit var adapter: DPRPartaiAdapter

    @Inject
    override lateinit var presenter: PerhitunganDPRPresenter
    private var realCountType: String? = null
    private var tps: TPS? = null

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        failed_alert.visibility = View.GONE
        empty_alert.visibility = View.GONE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        realCountType = intent.getStringExtra(REAL_COUNT_TYPE)
        tps = intent.getSerializableExtra(TPS_DATA) as TPS
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_dpr
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var title = ""
        when (realCountType) {
            "dpr" -> title = "DPR RI"
            "provinsi" -> title = "DPRD PROVINSI"
            "kabupaten" -> title = "DPRD KABUPATEN"
        }
        setupToolbar(true, title, R.color.white, 4f)
        setupDPRList()
        realCountType?.let { tps?.let { it1 -> presenter.getRealCountList(it1, it) } }
        no_vote_inc_button.setOnClickListener {
            val count = no_vote_count_field.text.toString().toInt()
            no_vote_count_field.setText(count.plus(1).toString())
        }
        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPRList() {
        adapter = DPRPartaiAdapter()
        dpr_list.layoutManager = LinearLayoutManager(this)
        dpr_list.adapter = adapter
    }

    override fun bindDapilData(dapil: Dapil) {
        dapil_name.text = "Dapil: ${dapil.nama}"
    }

    override fun showGetDapilFailedAlert() {
        failed_alert.visibility = View.VISIBLE
    }

    override fun bindCandidates(data: MutableList<CandidateData>) {
        adapter.setDatas(data as MutableList<ItemModel>)
    }

    override fun showGetRealCountListFailedAlert() {
        failed_alert.visibility = View.VISIBLE
        empty_alert.visibility = View.GONE
    }

    override fun showEmptyRealCountList() {
        failed_alert.visibility = View.GONE
        empty_alert.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
