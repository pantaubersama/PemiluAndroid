package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

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
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRCandidateAdapter
import com.pantaubersama.app.utils.PantauConstants.Merayakan.REAL_COUNT_TYPE
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import com.pantaubersama.app.utils.RxSchedulers
import kotlinx.android.synthetic.main.activity_perhitungan_dpd.*
import javax.inject.Inject

class PerhitunganDPDActivity : BaseActivity<PerhitunganDPDPresenter>(), PerhitunganDPDView {
    private lateinit var adapter: DPRCandidateAdapter
    private var candidateSelectedPosition: Int? = null

    @Inject
    override lateinit var presenter: PerhitunganDPDPresenter

    @Inject
    lateinit var rxSchedulers: RxSchedulers

    private var tps: TPS? = null
    private var realCountType: String? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_dpd
    }

    override fun fetchIntentExtra() {
        tps = intent.getSerializableExtra(TPS_DATA) as TPS
        realCountType = intent.getStringExtra(REAL_COUNT_TYPE)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "DPD", R.color.white, 4f)
        setupDPDList()
        tps?.let {
            realCountType?.let { type ->
                presenter.getDPDData(it, type)
            }
        }
        no_vote_inc_button.setOnClickListener {
            val count = no_vote_count_field.text.toString().toInt()
            no_vote_count_field.setText(count.plus(1).toString())
        }
        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPDList() {
        adapter = DPRCandidateAdapter(rxSchedulers)
        adapter.listener = object : DPRCandidateAdapter.Listener {
            override fun onCandidateCountChange(candidateUndoPosition: Int) {
                candidateSelectedPosition = candidateUndoPosition
            }
        }
        dpd_list.layoutManager = LinearLayoutManager(this@PerhitunganDPDActivity)
        dpd_list.adapter = adapter
    }

    override fun bindCandidates(candidates: MutableList<Candidate>) {
        adapter.setDatas(candidates as MutableList<ItemModel>)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        failed_alert.visibility = View.GONE
        empty_alert.visibility = View.GONE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun bindDapilData(dapil: Dapil) {
        dapil_name.text = "Dapil: ${dapil.nama}"
    }

    override fun showGetDapilFailedAlert() {
        failed_alert.visibility = View.VISIBLE
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
                candidateSelectedPosition?.let { adapter.undoCandidate(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
