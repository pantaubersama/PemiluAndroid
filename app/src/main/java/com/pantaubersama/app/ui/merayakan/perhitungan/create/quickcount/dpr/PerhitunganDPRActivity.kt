package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpr

import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.Dapil
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRPartaiAdapter
import com.pantaubersama.app.utils.PantauConstants.Merayakan.REAL_COUNT_TYPE
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import com.pantaubersama.app.utils.RxSchedulers
import kotlinx.android.synthetic.main.activity_perhitungan_dpr.*
import javax.inject.Inject
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.utils.MinMaxInputFilter
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import io.reactivex.Observable
import kotlinx.android.synthetic.main.data_sah_tidak_sah_layout.*

class PerhitunganDPRActivity : BaseActivity<PerhitunganDPRPresenter>(), PerhitunganDPRView {
    private lateinit var adapter: DPRPartaiAdapter

    @Inject
    override lateinit var presenter: PerhitunganDPRPresenter
    @Inject
    lateinit var rxSchedulers: RxSchedulers
    private var realCountType: String? = null
    private var tps: TPS? = null
    private var partySelectedPosition: Int? = null
    private var candidateSelectedPosition: Int? = null
    private var undoType: String = ""

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
            if (tps?.status != "published") {
                val count = if (no_vote_count_field.text.isNotEmpty()) {
                    no_vote_count_field.text.toString().toLong()
                } else {
                    0
                }

                no_vote_count_field.setText(count.plus(1).toString())
            } else {
                ToastUtil.show(this@PerhitunganDPRActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
            }
        }
        no_vote_count_field.isEnabled = tps?.status != "published"
        no_vote_count_field.filters = arrayOf<InputFilter>(MinMaxInputFilter("0", "500"))
        RxTextView.textChanges(no_vote_count_field)
            .skipInitialValue()
            .flatMap {
                if (it.isEmpty()) {
                    Observable.just("0")
                } else {
                    Observable.just(it)
                }
            }
            .map {
                it.toString()
            }
            .map {
                it.toLong()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext { noVote ->
                invalid_vote_count.text = noVote.toString()
                val allCount = valid_vote_count.text.toString().toLong() + invalid_vote_count.text.toString().toLong()
                all_vote_count.text = allCount.toString()
                if (tps?.status != "published") {
                    tps?.id?.let {
                        realCountType?.let { it1 ->
                            presenter.saveRealCount(
                                it,
                                it1,
                                (adapter.getListData() as MutableList<CandidateData>),
                                noVote
                            )
                        }
                    }
                }
                Handler().postDelayed({
                    adapter.undoRedoToolses.add(UndoRedoTools(no_vote_count_field))
                    undoType = "invalid"
                    }, 500)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPRList() {
        adapter = DPRPartaiAdapter(rxSchedulers, tps?.status != "published")
        adapter.listener = object : DPRPartaiAdapter.Listener {
            override fun saveRealCount(items: MutableList<CandidateData>, selectedPartyPosition: Int) {
                val allValidCounts: MutableList<Long> = ArrayList()
                items.forEachIndexed { index, candidateData ->
                    allValidCounts.add(candidateData.totalCount)
                }
                try {
                    val validCount = allValidCounts.sum()
                    valid_vote_count.text = validCount.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val allCount = allValidCounts.sum().toString().toLong() + invalid_vote_count.text.toString().toLong()
                    all_vote_count.text = allCount.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (tps?.status != "published") {
                    realCountType?.let {
                        tps?.id?.let { it1 ->
                            presenter.saveRealCount(
                                it1,
                                it,
                                items
                            )
                        }
                    }
                }
                undoType = "party"
                partySelectedPosition = selectedPartyPosition
            }

            override fun onCandidateChanged(partyPosition: Int, candidateUndoPosition: Int) {
                undoType = "candidate"
                partySelectedPosition = partyPosition
                candidateSelectedPosition = candidateUndoPosition
            }
        }
        dpr_list.layoutManager = LinearLayoutManager(this)
        (dpr_list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        dpr_list.adapter = adapter
    }

    override fun bindDapilData(dapil: Dapil) {
        dapil_name.text = "Dapil: ${dapil.nama}"
    }

    override fun showGetDapilFailedAlert() {
        failed_alert.visibility = View.VISIBLE
    }

    override fun bindCandidates(data: MutableList<CandidateData>) {
        no_vote_container.visibility = View.VISIBLE
        adapter.setDatas(data as MutableList<ItemModel>)
        tps?.id?.let {
            realCountType?.let { it1 ->
                tps?.status?.let { it2 ->
                    presenter.getRealCount(it, it1, it2)
                }
            }
        }
    }

    override fun showFailedGetRealCountAlert(message: String?) {
        if (message != "Belum ada perhitungan") {
            ToastUtil.show(this@PerhitunganDPRActivity, "Gagal memuat perhitungan")
        }
    }

    override fun showGetRealCountListFailedAlert() {
        failed_alert.visibility = View.VISIBLE
        empty_alert.visibility = View.GONE
    }

    override fun showEmptyRealCountList() {
        failed_alert.visibility = View.GONE
        empty_alert.visibility = View.VISIBLE
    }

    override fun onSuccessSaveRealCount() {
//        Timber.d("saved")
    }

    override fun bindRealCount(realCount: RealCount) {
        no_vote_count_field.setText(realCount.invalidVote.toString())
        val totalValidCount: MutableList<Long> = ArrayList()
        realCount.parties.forEachIndexed { index, party ->
            totalValidCount.add(party.totalVote)
        }
        valid_vote_count.text = totalValidCount.sum().toString()
        val totalCount = totalValidCount.sum().toString().toLong() + invalid_vote_count.text.toString().toLong()
        all_vote_count.text = totalCount.toString()
        adapter.updateData(realCount)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
                undo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun undo() {
        when (undoType) {
            "invalid" -> adapter.undoRedoToolses[adapter.getListData().size].undo()
            "party" -> partySelectedPosition?.let { partyPos ->
                adapter.undoParty(partyPos)
            }
            "candidate" -> partySelectedPosition?.let { partyPos ->
                candidateSelectedPosition?.let { candidatePost ->
                    adapter.undoCandidate(partyPos, candidatePost)
                }
            }
        }
    }
}
