package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import kotlinx.android.synthetic.main.activity_perhitungan_presiden.*
import kotlinx.android.synthetic.main.data_sah_tidak_sah_layout.*
import javax.inject.Inject

class PerhitunganPresidenActivity : BaseActivity<PerhitunganPresidenPresenter>(), PerhitunganPresidenView, View.OnClickListener {
    @Inject
    override lateinit var presenter: PerhitunganPresidenPresenter
    @Inject
    lateinit var rxSchedulers: RxSchedulers

    private lateinit var undoRedoToolses: MutableList<UndoRedoTools>
    private var undoPosition: Int? = null
    private var tps: TPS? = null
    private var realCountType = "presiden"

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
        undoRedoToolses = ArrayList()
        setupCandidate1()
        setupCandidate2()
        setupNoVotes()
        tps?.id?.let {
            tps?.status?.let { it1 ->
                presenter.getRealCount(it, it1, realCountType)
            }
        }

        save_button.setOnClickListener(this)
    }

    override fun showFailedGetRealCountAlert(message: String?) {
        if (message != "Belum ada perhitungan") {
            ToastUtil.show(this@PerhitunganPresidenActivity, "Gagal memuat perhitungan presiden")
        }
    }

    private fun setupCandidate1() {
        if (tps?.status == "published") {
            candidate_1_count_field.isEnabled = false
//            candidate_1_inc_button.isEnabled = false
        }
        candidate_1_inc_button.setOnClickListener(this)
//        candidate_1_count_field.onFocusChangeListener = this
        RxTextView.textChanges(candidate_1_count_field)
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
                undoRedoToolses.add(0, UndoRedoTools(candidate_1_count_field))
                undoPosition = 0
                if (tps?.status == "draft" || tps?.status == "sandbox") {
                    tps?.id?.let { it1 -> presenter.saveCandidate1Count(it, it1, realCountType) }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupCandidate2() {
        if (tps?.status == "published") {
            candidate_2_count_field.isEnabled = false
//            candidate_2_inc_button.isEnabled = false
        }
        candidate_2_inc_button.setOnClickListener(this)
//        candidate_1_count_field.onFocusChangeListener = this
        RxTextView.textChanges(candidate_2_count_field)
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
                undoRedoToolses.add(1, UndoRedoTools(candidate_2_count_field))
                undoPosition = 1
                if (tps?.status == "draft" || tps?.status == "sandbox") {
                    tps?.id?.let { it1 -> presenter.saveCandidate2Count(it, it1, realCountType) }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupNoVotes() {
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
                undoRedoToolses.add(2, UndoRedoTools(no_vote_count_field))
                undoPosition = 2
                if (tps?.status == "draft" || tps?.status == "sandbox") {
                    tps?.id?.let { it1 -> presenter.saveInvalidVoteCount(it, it1, realCountType) }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    override fun onSuccessVoteCandidateCount() {
        tps?.id?.let {
            presenter.getCounter(it, realCountType)
        }
    }

    override fun bindCounter(validCount: Long, invalidCount: Long, allCount: Long) {
        valid_vote_count.text = validCount.toString()
        invalid_vote_count.text = invalidCount.toString()
        all_vote_count.text = allCount.toString()
    }

    override fun onClick(view: View) {
        when (view) {
            candidate_1_inc_button -> {
                if (tps?.status == "published") {
                    ToastUtil.show(this@PerhitunganPresidenActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                } else {
                    val count = if (candidate_1_count_field.text.isNotEmpty()) {
                        candidate_1_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    candidate_1_count_field.setText(count.plus(1).toString())
                    undoPosition = 0
                }
            }
            candidate_2_inc_button -> {
                if (tps?.status == "published") {
                    ToastUtil.show(this@PerhitunganPresidenActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                } else {
                    val count = if (candidate_2_count_field.text.isNotEmpty()) {
                        candidate_2_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    undoPosition = 1
                    candidate_2_count_field.setText(count.plus(1).toString())
                }
            }
            no_vote_inc_button -> {
                if (tps?.status == "published") {
                    ToastUtil.show(this@PerhitunganPresidenActivity, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                } else {
                    val count = if (no_vote_count_field.text.isNotEmpty()) {
                        no_vote_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    undoPosition = 2
                    no_vote_count_field.setText(count.plus(1).toString())
                }
            }
            save_button -> {
                finish()
            }
        }
    }

//    override fun onFocusChange(view: View, isFocus: Boolean) {
//        when (view) {
//            candidate_1_count_field -> {
//                if (isFocus) {
//                    undoPosition = 0
//                }
//            }
//            candidate_2_count_field -> {
//                if (isFocus) {
//                    undoPosition = 1
//                }
//            }
//            no_vote_count_field -> {
//                if (isFocus) {
//                    undoPosition = 2
//                }
//            }
//        }
//    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun bindRealCount(realCount: RealCount?) {
        realCount?.let {
            candidate_1_count_field.setText(it.candidates[0].totalVote.toString())
            candidate_2_count_field.setText(it.candidates[1].totalVote.toString())
            no_vote_count_field.setText(it.invalidVote.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
                undoPosition?.let {
                    undoRedoToolses[it].undo()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showFailedSaveDataAlert() {
        ToastUtil.show(this@PerhitunganPresidenActivity, "Gagal menyimpan data")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
