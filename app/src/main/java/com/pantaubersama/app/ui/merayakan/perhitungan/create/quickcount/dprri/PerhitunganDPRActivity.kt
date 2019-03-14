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
import com.pantaubersama.app.data.model.tps.RealCount
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
                it.toInt()
            }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnNext { noVote ->
                invalid_vote_count.text = noVote.toString()
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
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPRList() {
        adapter = DPRPartaiAdapter(rxSchedulers)
        adapter.listener = object : DPRPartaiAdapter.Listener {
            override fun saveRealCount(items: MutableList<CandidateData>) {
                val allCounts: MutableList<Int> = ArrayList()
                items.forEachIndexed { index, candidateData ->
                    allCounts.add(candidateData.totalCount)
                }
                try {
                    valid_vote_count.text = allCounts.sum().toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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
                presenter.getRealCount(it, it1)
            }
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
        adapter.updateData(realCount)
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
