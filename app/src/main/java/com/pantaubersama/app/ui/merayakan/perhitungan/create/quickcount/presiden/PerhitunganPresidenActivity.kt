package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.RealCount
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
        if (tps?.id != null) {
            tps?.id?.let {
                presenter.getRealCount(it)
            }
        }

        save_button.setOnClickListener(this)
    }

    private fun setupCandidate1() {
        undoRedoToolses.add(0, UndoRedoTools(candidate_1_count_field))
        candidate_1_inc_button.setOnClickListener(this)
//        candidate_1_count_field.onFocusChangeListener = this
        RxTextView.textChanges(candidate_1_count_field)
            .filter {
                it.isNotEmpty()
            }
            .map {
                it.toString()
            }
            .map {
                it.toInt()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext {
                undoPosition = 0
                tps?.id?.let { it1 -> presenter.saveCandidate1Count(it, it1) }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupCandidate2() {
        undoRedoToolses.add(1, UndoRedoTools(candidate_2_count_field))
        candidate_2_inc_button.setOnClickListener(this)
//        candidate_1_count_field.onFocusChangeListener = this
        RxTextView.textChanges(candidate_2_count_field)
            .filter {
                it.isNotEmpty()
            }
            .map {
                it.toString()
            }
            .map {
                it.toInt()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext {
                undoPosition = 1
                tps?.id?.let { it1 -> presenter.saveCandidate2Count(it, it1) }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupNoVotes() {
        undoRedoToolses.add(2, UndoRedoTools(no_vote_count_field))
        no_vote_inc_button.setOnClickListener(this)
//        no_vote_count_field.onFocusChangeListener = this
        RxTextView.textChanges(no_vote_count_field)
            .filter {
                it.isNotEmpty()
            }
            .map {
                it.toString()
            }
            .map {
                it.toInt()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext {
                undoPosition = 2
                tps?.id?.let { it1 -> presenter.saveInvalidVoteCount(it, it1) }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    override fun onSuccessVoteCandidateCount() {
        tps?.id?.let {
            presenter.getCounter(it)
        }
    }

    override fun bindCounter(validCount: Int, invalidCount: Int, allCount: Int) {
        valid_vote_count.text = validCount.toString()
        invalid_vote_count.text = invalidCount.toString()
        all_vote_count.text = allCount.toString()
    }

    override fun onClick(view: View) {
        when (view) {
            candidate_1_inc_button -> {
                val count = candidate_1_count_field.text.toString().toInt()
                undoPosition = 0
                candidate_1_count_field.setText(count.plus(1).toString())
            }
            candidate_2_inc_button -> {
                val count = candidate_2_count_field.text.toString().toInt()
                undoPosition = 1
                candidate_2_count_field.setText(count.plus(1).toString())
            }
            no_vote_inc_button -> {
                val count = no_vote_count_field.text.toString().toInt()
                undoPosition = 2
                no_vote_count_field.setText(count.plus(1).toString())
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
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
