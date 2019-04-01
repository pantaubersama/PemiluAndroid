package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.Dapil
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRCandidateAdapter
import com.pantaubersama.app.utils.MinMaxInputFilter
import com.pantaubersama.app.utils.PantauConstants.Merayakan.REAL_COUNT_TYPE
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_perhitungan_dpd.*
import kotlinx.android.synthetic.main.data_sah_tidak_sah_layout.*
import javax.inject.Inject

class PerhitunganDPDActivity : BaseActivity<PerhitunganDPDPresenter>(), PerhitunganDPDView {
    private lateinit var adapter: DPRCandidateAdapter
    private var candidateSelectedPosition: Int? = null
    private lateinit var undoRedoTools: UndoRedoTools
    private var undoType: String = ""

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
        no_vote_count_field.isEnabled = tps?.status != "published"
        no_vote_inc_button.setOnClickListener {
            if (tps?.status != "published") {
                val count = if (no_vote_count_field.text.isNotEmpty()) {
                    no_vote_count_field.text.toString().toLong()
                } else {
                    0
                }

                no_vote_count_field.setText(count.plus(1).toString())
            } else {
                ToastUtil.show(this@PerhitunganDPDActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
            }
        }
        no_vote_count_field.filters = arrayOf<InputFilter>(MinMaxInputFilter("0", "500"))
        undoRedoTools = UndoRedoTools(no_vote_count_field)
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
                                (adapter.getListData() as MutableList<Candidate>),
                                noVote
                            )
                        }
                    }
                }
                Handler().postDelayed({  /* @edityo 5/3/19  delayed bcs response getItemChallenge not changed immediately */
                    undoType = "invalid"
                    adapter.undoRedoToolses.add(undoRedoTools)
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

    override fun onSuccessSaveRealCount() {
    }

    private fun setupDPDList() {
        adapter = DPRCandidateAdapter(
            rxSchedulers,
            tps?.status != "published"
        )
        adapter.listener = object : DPRCandidateAdapter.Listener {
            override fun onCandidateCountChange(candidateUndoPosition: Int) {
                val allValidCount: MutableList<Long> = ArrayList()
                adapter.getListData().forEachIndexed { index, candidate ->
                    allValidCount.add((candidate as Candidate).candidateCount)
                }
                try {
                    valid_vote_count.text = allValidCount.sum().toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val allCount = valid_vote_count.text.toString().toLong() + invalid_vote_count.text.toString().toLong()
                    all_vote_count.text = allCount.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (tps?.status != "published") {
                    tps?.id?.let {
                        realCountType?.let { it1 ->
                            presenter.saveRealCount(
                                it,
                                it1,
                                (adapter.getListData() as MutableList<Candidate>)
                            )
                        }
                    }
                }
                undoType = "candidate"
                candidateSelectedPosition = candidateUndoPosition
            }
        }
        dpd_list.layoutManager = LinearLayoutManager(this@PerhitunganDPDActivity)
        dpd_list.adapter = adapter
    }

    override fun bindCandidates(candidates: MutableList<Candidate>) {
        no_vote_container.visibility = View.VISIBLE
        adapter.setDatas(candidates as MutableList<ItemModel>)
        tps?.id?.let { realCountType?.let { it1 -> tps?.status?.let { it2 -> presenter.getRealCount(it, it1, it2) } } }
    }

    override fun showFailedGetRealCountAlert(message: String?) {
        if (message != "Belum ada perhitungan") {
            ToastUtil.show(this@PerhitunganDPDActivity, "Gagal memuat perhitungan DPD")
        }
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

    override fun bindRealCount(realCount: RealCount) {
        no_vote_count_field.setText(realCount.invalidVote.toString())
        val totalValidCount: MutableList<Long> = ArrayList()
        realCount.candidates.forEachIndexed { index, candidate ->
            totalValidCount.add(candidate.totalVote)
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
        if (undoType == "invalid") {
            adapter.undoRedoToolses[adapter.getListData().size].undo()
        } else if (undoType == "candidate") {
            candidateSelectedPosition?.let { adapter.undoCandidate(it) }
        }
    }
}
