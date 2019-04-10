package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.realcount.Candidate
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_perhitungan_presiden.*
import kotlinx.android.synthetic.main.data_sah_tidak_sah_layout.*
import javax.inject.Inject
import com.pantaubersama.app.utils.* // ktlint-disable

class PerhitunganPresidenActivity : BaseActivity<PerhitunganPresidenPresenter>(), PerhitunganPresidenView, View.OnClickListener {
    @Inject
    override lateinit var presenter: PerhitunganPresidenPresenter
    @Inject
    lateinit var rxSchedulers: RxSchedulers

    private lateinit var adapter: PresidenAdapter
    private var tps: TPS? = null
    private var realCountType = "presiden"
    private var undoType = ""
    private var undoPosition: Int? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(PantauConstants.Merayakan.TPS_DATA)?.let {
            tps = it as TPS
        }
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_presiden
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Presiden", R.color.white, 4f)
        setupPresidenList()
        setupNoVotes()
        tps?.id?.let {
            tps?.status?.let { it1 ->
                presenter.getRealCount(it, it1, realCountType)
            }
        }

        save_button.setOnClickListener(this)
    }

    private fun setupNoVotes() {
        no_vote_count_field.filters = arrayOf<InputFilter>(MinMaxInputFilter("0", "500"))
        if (tps?.status == "published") {
            no_vote_count_field.isEnabled = false
//            no_vote_inc_button.isEnabled = false
        }
        no_vote_inc_button.setOnClickListener(this)
//        no_vote_count_field.onFocusChangeListener = this
        RxTextView.textChanges(no_vote_count_field)
            .skipInitialValue()
            .filter {
                it.isNotEmpty()
            }
            .map {
                it.toString()
            }
            .map {
                it.toLong()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext {
                if (tps?.status == "local" || tps?.status == "sandbox") {
                    tps?.id?.let { it1 -> presenter.saveCandidateCount(
                        (adapter.getListData()[0] as Candidate).totalVote,
                        (adapter.getListData()[1] as Candidate).totalVote,
                        it,
                        it1, realCountType) }
                }
                Handler().postDelayed({
                    undoType = "invalid"
                    adapter.undoRedoToolses.add(UndoRedoTools(no_vote_count_field))
                }, 500)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupPresidenList() {
        adapter = PresidenAdapter(rxSchedulers, tps?.status != "published")
        adapter.listener = object : PresidenAdapter.Listener {
            override fun saveRealCount(adapterPosition: Int) {
                undoType = "item"
                undoPosition = adapterPosition
                if (tps?.status == "local" || tps?.status == "sandbox") {
                    tps?.id?.let {
                        presenter.saveCandidateCount(
                            (adapter.getListData()[0] as Candidate).totalVote,
                            (adapter.getListData()[1] as Candidate).totalVote,
                            no_vote_count_field.text.toString().toLong(),
                            it,
                            realCountType
                        )
                    }
                }
            }
        }
        presiden_list.layoutManager = GridLayoutManager(this@PerhitunganPresidenActivity, 2)
        presiden_list.adapter = adapter

        adapter.setDatas(getData())
    }

    private fun getData(): List<ItemModel> {
        val datas: MutableList<Candidate> = ArrayList()
        datas.add(Candidate(1, 0, 0, "presiden", 0.0))
        datas.add(Candidate(2, 0, 0, "presiden", 0.0))
        return datas
    }

    override fun showFailedGetRealCountAlert(message: String?) {
        if (message != "Belum ada perhitungan") {
            ToastUtil.show(this@PerhitunganPresidenActivity, "Gagal memuat perhitungan presiden")
        }
    }

    override fun onSuccessVoteCandidateCount() {
        tps?.id?.let {
            presenter.getCounter(it, realCountType)
        }
    }

    override fun bindCounter(realCount: RealCount) {
        val validCount = realCount.candidates[0].totalVote + realCount.candidates[1].totalVote
        val allCount = validCount + realCount.invalidVote
        valid_vote_count.text = validCount.toString()
        invalid_vote_count.text = realCount.invalidVote.toString()
        all_vote_count.text = allCount.toString()
    }

    override fun onClick(view: View) {
        when (view) {
            no_vote_inc_button -> {
                if (tps?.status == "published") {
                    ToastUtil.show(this@PerhitunganPresidenActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                } else {
                    val count = if (no_vote_count_field.text.isNotEmpty()) {
                        no_vote_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    no_vote_count_field.setText(count.plus(1).toString())
                }
            }
            save_button -> {
                finishSection()
            }
        }
    }

    private fun finishSection() {
        if (all_vote_count.text.toString().toLong() <= 500) {
            finish()
        } else {
            ToastUtil.show(this@PerhitunganPresidenActivity, "Total suara (${all_vote_count.text}) melebihi 500")
        }
    }

    override fun onBackPressed() {
        finishSection()
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun bindRealCount(realCount: RealCount?) {
        realCount?.let {
            no_vote_count_field.setText(realCount.invalidVote.toString())
            if (tps?.status == "published") {
                invalid_vote_count.text = realCount.invalidVote.toString()
                val totalVote: MutableList<Long> = ArrayList()
                realCount.candidates.forEach { candidate ->
                    totalVote.add(candidate.totalVote)
                }
                valid_vote_count.text = totalVote.sum().toString()
                all_vote_count.text = (totalVote.sum() + realCount.invalidVote).toString()
            }
            adapter.setVote(it)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_undo, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.undo_action -> {
//                when (undoType) {
//                    "invalid" -> adapter.undoRedoToolses[adapter.getListData().size].undo()
//                    "item" -> undoPosition?.let {
//                        adapter.undoPresiden(it)
//                    }
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun showFailedSaveDataAlert() {
        ToastUtil.show(this@PerhitunganPresidenActivity, "Gagal menyimpan data")
    }
}