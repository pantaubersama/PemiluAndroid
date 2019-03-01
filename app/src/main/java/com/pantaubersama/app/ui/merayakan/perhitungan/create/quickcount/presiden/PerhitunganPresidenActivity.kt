package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.RxSchedulers
import kotlinx.android.synthetic.main.activity_perhitungan_presiden.*
import javax.inject.Inject

class PerhitunganPresidenActivity : BaseActivity<PerhitunganPresidenPresenter>(), PerhitunganPresidenView, View.OnClickListener {
    @Inject
    override lateinit var presenter: PerhitunganPresidenPresenter
    @Inject
    lateinit var rxSchedulers: RxSchedulers

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_presiden
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Presiden", R.color.white, 4f)
        candidate_1_inc_button.setOnClickListener(this)
        candidate_2_inc_button.setOnClickListener(this)
        no_vote_inc_button.setOnClickListener(this)
        RxTextView.textChanges(candidate_1_count_field)
            .filter {
                it != ""
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
                presenter.saveCandidate1Count(it)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(candidate_2_count_field)
            .filter {
                it != ""
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
                presenter.saveCandidate2Count(it)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(no_vote_count_field)
            .filter {
                it != ""
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
                presenter.saveInvalidVoteCount(it)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
        save_button.setOnClickListener(this)
    }

    override fun onSuccessVoteCandidateCount(validCount: Int, invalidCount: Int, allCount: Int) {
        valid_vote_count.text = validCount.toString()
        invalid_vote_count.text = invalidCount.toString()
        all_vote_count.text = allCount.toString()
    }

    override fun onClick(view: View) {
        when (view) {
            candidate_1_inc_button -> {
                val count = candidate_1_count_field.text.toString().toInt()
                candidate_1_count_field.setText(count.plus(1).toString())
            }
            candidate_2_inc_button -> {
                val count = candidate_2_count_field.text.toString().toInt()
                candidate_2_count_field.setText(count.plus(1).toString())
            }
            no_vote_inc_button -> {
                val count = no_vote_count_field.text.toString().toInt()
                no_vote_count_field.setText(count.plus(1).toString())
            }
            save_button -> {
                finish()
            }
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
