package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.C1Form
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_c1_form.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class C1FormActivity : BaseActivity<C1FormPresenter>(), C1FormView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_c1_form
    }

    @Inject
    override lateinit var presenter: C1FormPresenter
    @Inject
    lateinit var rxSchedulers: RxSchedulers
    private var c1Type: String? = null
    private var tps: TPS? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        c1Type = intent.getStringExtra(PantauConstants.Merayakan.C1_MODEL_TYPE)
        tps = intent.getSerializableExtra(PantauConstants.Merayakan.TPS_DATA) as TPS
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var title: String = ""
        when (c1Type) {
            "presiden" -> {
                title = "Model C1 Presiden"
                c1_short_hint.text = getString(R.string.c1_form_presiden_hint)
            }
            "dpr_ri" -> {
                title = "Model C1-DPR RI"
                c1_short_hint.text = getString(R.string.c1_form_dpr_ri_hint)
            }
            "dpd" -> {
                title = "Model C1-DPD RI"
                c1_short_hint.text = getString(R.string.c1_form_dpd_ri_hint)
            }
            "dprd_provinsi" -> {
                title = "Model C1-DPRD Provinsi"
                c1_short_hint.text = getString(R.string.c1_form_dprd_provinsi_hint)
            }
            "dprd_kabupaten" -> {
                title = "Model C1-DPRD Kabupaten"
                c1_short_hint.text = getString(R.string.c1_form_dprd_kabupaten_hint)
            }
        }
        setupToolbar(false, title, R.color.white, 4f)
        setupAllSection1()
        setupAllSection2()
        setupAllDisabilitasSection()
        setupSuratSuara()
        tps?.id?.let { c1Type?.let { it1 -> presenter.getC1Data(it, it1) } }
    }

    private fun setupSuratSuara() {
        RxTextView.textChanges(rejected_documents_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (unused_documents_count.text.isNotEmpty()) {
                    unused_documents_count.getInt()
                } else {
                    0
                } + if (used_documents_count.text.isNotEmpty()) {
                    used_documents_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                accepted_documents_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(unused_documents_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (rejected_documents_count.text.isNotEmpty()) {
                    rejected_documents_count.getInt()
                } else {
                    0
                } + if (used_documents_count.text.isNotEmpty()) {
                    used_documents_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                accepted_documents_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(used_documents_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (unused_documents_count.text.isNotEmpty()) {
                    unused_documents_count.getInt()
                } else {
                    0
                } + if (used_documents_count.text.isNotEmpty()) {
                    used_documents_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                accepted_documents_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupAllDisabilitasSection() {
        setupPemilihDisabilitas()
        setupHakPilihDisabilitas()
    }

    private fun setupHakPilihDisabilitas() {
        RxTextView.textChanges(disabilitas_l_voted_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (disabilitas_p_voted_count.text.isNotEmpty()) {
                    disabilitas_p_voted_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                disabilitas_total_voted_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(disabilitas_p_voted_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (disabilitas_l_voted_count.text.isNotEmpty()) {
                    disabilitas_l_voted_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                disabilitas_total_voted_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(disabilitas_total_count)
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
            .debounce(1000, TimeUnit.MILLISECONDS)
            .doOnNext {
                tps?.id?.let {
                    c1Type?.let { it1 ->
                        presenter.saveDisabilitas(
                            it,
                            disabilitas_l_count.getInt(),
                            disabilitas_p_count.getInt(),
                            it1
                        )
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupPemilihDisabilitas() {
        RxTextView.textChanges(disabilitas_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (disabilitas_p_count.text.isNotEmpty()) {
                    disabilitas_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                disabilitas_total_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(disabilitas_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (disabilitas_l_count.text.isNotEmpty()) {
                    disabilitas_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                disabilitas_total_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(disabilitas_total_voted_count)
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
            .debounce(1000, TimeUnit.MILLISECONDS)
            .doOnNext {
                tps?.id?.let {
                    c1Type?.let { it1 ->
                        presenter.saveHakPilihDisabilitas(
                            it,
                            disabilitas_l_voted_count.getInt(),
                            disabilitas_p_voted_count.getInt(),
                            it1
                        )
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupAllSection2() {
        setupDptC7()
        setupDptBC7()
        setupDpkC7()
        setupTotalSection2()
    }

    private fun setupTotalSection2() {
        RxTextView.textChanges(hak_pilih_a1_a2_a3_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (hak_pilih_a1_a2_a3_p_count.text.isNotEmpty()) {
                    hak_pilih_a1_a2_a3_p_count.getInt()
                } else {
                    0
                }
                val newAllCount = it + lpCount
                hak_pilih_a1_a2_a3_total_count.text = newAllCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(hak_pilih_a1_a2_a3_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (hak_pilih_a1_a2_a3_l_count.text.isNotEmpty()) {
                    hak_pilih_a1_a2_a3_l_count.getInt()
                } else {
                    0
                }
                val newAllCount = it + lpCount
                hak_pilih_a1_a2_a3_total_count.text = newAllCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(hak_pilih_a1_a2_a3_total_count)
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
            .debounce(1000, TimeUnit.MILLISECONDS)
            .doOnNext {
                tps?.id?.let {
                    c1Type?.let { it1 ->
                        presenter.saveSection2(
                            it,
                            dpt_c7_l_count.getInt(),
                            dpt_c7_p_count.getInt(),
                            dpt_b_c7_l_count.getInt(),
                            dpt_b_c7_l_count.getInt(),
                            dpk_c7_l_count.getInt(),
                            dpk_c7_l_count.getInt(),
                            it1
                        )
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDpkC7() {
        RxTextView.textChanges(dpk_c7_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpk_c7_p_count.text.isNotEmpty()) {
                    dpk_c7_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpk_c7_total_count.text = newCount.toString()

                // total l
                val allLCount = if (dpt_c7_l_count.text.isNotEmpty()) {
                    dpt_c7_l_count.getInt()
                } else {
                    0
                } + if (dpt_b_c7_l_count.text.isNotEmpty()) {
                    dpt_b_c7_l_count.getInt()
                } else {
                    0
                }
                val newLCount = it + allLCount
                hak_pilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpk_c7_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpk_c7_l_count.text.isNotEmpty()) {
                    dpk_c7_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpk_c7_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_c7_p_count.text.isNotEmpty()) {
                    dpt_c7_p_count.getInt()
                } else {
                    0
                } + if (dpt_b_c7_p_count.text.isNotEmpty()) {
                    dpt_b_c7_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                hak_pilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDptBC7() {
        RxTextView.textChanges(dpt_b_c7_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_b_c7_p_count.text.isNotEmpty()) {
                    dpt_b_c7_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_c7_total_count.text = newCount.toString()

                // total l
                val allLCount = if (dpt_c7_l_count.text.isNotEmpty()) {
                    dpt_c7_l_count.getInt()
                } else {
                    0
                } + if (dpk_c7_l_count.text.isNotEmpty()) {
                    dpk_c7_l_count.getInt()
                } else {
                    0
                }
                val newLCount = it + allLCount
                hak_pilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_b_c7_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_b_c7_l_count.text.isNotEmpty()) {
                    dpt_b_c7_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_c7_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_c7_p_count.text.isNotEmpty()) {
                    dpt_c7_p_count.getInt()
                } else {
                    0
                } + if (dpk_c7_p_count.text.isNotEmpty()) {
                    dpk_c7_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                hak_pilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDptC7() {
        RxTextView.textChanges(dpt_c7_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_c7_p_count.text.isNotEmpty()) {
                    dpt_c7_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_c7_total_count.text = newCount.toString()

                // total l
                val allLCount = if (dpt_b_c7_l_count.text.isNotEmpty()) {
                    dpt_b_c7_l_count.getInt()
                } else {
                    0
                } + if (dpk_c7_l_count.text.isNotEmpty()) {
                    dpk_c7_l_count.getInt()
                } else {
                    0
                }
                val newLCount = it + allLCount
                hak_pilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_c7_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_c7_l_count.text.isNotEmpty()) {
                    dpt_c7_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_c7_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_b_c7_p_count.text.isNotEmpty()) {
                    dpt_b_c7_p_count.getInt()
                } else {
                    0
                } + if (dpk_c7_p_count.text.isNotEmpty()) {
                    dpk_c7_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                hak_pilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupAllSection1() {
        setupDptA3()
        setupDptA4()
        setupDpk()
        setupTotalSection1()
    }

    private fun setupTotalSection1() {
        RxTextView.textChanges(pemilih_a1_a2_a3_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (pemilih_a1_a2_a3_p_count.text.isNotEmpty()) {
                    pemilih_a1_a2_a3_p_count.getInt()
                } else {
                    0
                }
                val newAllCount = it + lpCount
                pemilih_a1_a2_a3_total_count.text = newAllCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(pemilih_a1_a2_a3_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (pemilih_a1_a2_a3_l_count.text.isNotEmpty()) {
                    pemilih_a1_a2_a3_l_count.getInt()
                } else {
                    0
                }
                val newAllCount = it + lpCount
                pemilih_a1_a2_a3_total_count.text = newAllCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(pemilih_a1_a2_a3_total_count)
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
            .debounce(1000, TimeUnit.MILLISECONDS)
            .doOnNext {
                tps?.id?.let {
                    c1Type?.let { it1 ->
                        presenter.saveSection1(
                            it,
                            dpt_a3_kpu_l_count.getInt(),
                            dpt_a3_kpu_p_count.getInt(),
                            dpt_b_a4_kpu_l_count.getInt(),
                            dpt_b_a4_kpu_p_count.getInt(),
                            dpk_a_kpu_l_count.getInt(),
                            dpk_a_kpu_p_count.getInt(),
                            it1
                        )
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDpk() {
        RxTextView.textChanges(dpk_a_kpu_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpk_a_kpu_p_count.text.isNotEmpty()) {
                    dpk_a_kpu_p_count.getInt()
                } else {
                    0
                }
                val newAllCount = it + lpCount
                dpk_a_kpu_l_total_count.text = newAllCount.toString()

                // total l
                val allLCount = if (dpt_a3_kpu_l_count.text.isNotEmpty()) {
                    dpt_a3_kpu_l_count.getInt()
                } else {
                    0
                } + if (dpt_b_a4_kpu_l_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_l_count.getInt()
                } else {
                    0
                }

                val newLCount = it + allLCount
                pemilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpk_a_kpu_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpk_a_kpu_l_count.text.isNotEmpty()) {
                    dpk_a_kpu_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpk_a_kpu_l_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_a3_kpu_p_count.text.isNotEmpty()) {
                    dpt_a3_kpu_p_count.getInt()
                } else {
                    0
                } + if (dpt_b_a4_kpu_p_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                pemilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDptA4() {
        RxTextView.textChanges(dpt_b_a4_kpu_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_b_a4_kpu_p_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_a4_kpu_total_count.text = newCount.toString()

                // total l
                val allLCount = if (dpt_a3_kpu_l_count.text.isNotEmpty()) {
                    dpt_a3_kpu_l_count.getInt()
                } else {
                    0
                } + if (dpk_a_kpu_l_count.text.isNotEmpty()) {
                    dpk_a_kpu_l_count.getInt()
                } else {
                    0
                }

                val newLCount = it + allLCount
                pemilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_b_a4_kpu_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_b_a4_kpu_l_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_a4_kpu_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_a3_kpu_p_count.text.isNotEmpty()) {
                    dpt_a3_kpu_p_count.getInt()
                } else {
                    0
                } + if (dpk_a_kpu_p_count.text.isNotEmpty()) {
                    dpk_a_kpu_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                pemilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setupDptA3() {
        RxTextView.textChanges(dpt_a3_kpu_l_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_a3_kpu_p_count.text.isNotEmpty()) {
                    dpt_a3_kpu_p_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_a3_kpu_total_count.text = newCount.toString()

                // total l
                val allLCount = if (dpt_b_a4_kpu_l_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_l_count.getInt()
                } else {
                    0
                } + if (dpk_a_kpu_l_count.text.isNotEmpty()) {
                    dpk_a_kpu_l_count.getInt()
                } else {
                    0
                }
                val newLCount = it + allLCount
                pemilih_a1_a2_a3_l_count.text = newLCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_a3_kpu_p_count)
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
            .doOnNext {
                // total l+p
                val lpCount = if (dpt_a3_kpu_l_count.text.isNotEmpty()) {
                    dpt_a3_kpu_l_count.getInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_a3_kpu_total_count.text = newCount.toString()

                // total p
                val allPCount = if (dpt_b_a4_kpu_p_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_p_count.getInt()
                } else {
                    0
                } + if (dpk_a_kpu_p_count.text.isNotEmpty()) {
                    dpk_a_kpu_p_count.getInt()
                } else {
                    0
                }

                val newPCount = it + allPCount
                pemilih_a1_a2_a3_p_count.text = newPCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessSaveData() {
    }

    override fun showFailedSaveDataAlert() {
    }

    override fun bindC1Data(c1Form: C1Form) {
        dpt_a3_kpu_l_count.setText(c1Form.a3L.toString())
        dpt_a3_kpu_p_count.setText(c1Form.a3P.toString())
        dpt_b_a4_kpu_l_count.setText(c1Form.a4L.toString())
        dpt_b_a4_kpu_p_count.setText(c1Form.a4P.toString())
        dpk_a_kpu_l_count.setText(c1Form.aDpkL.toString())
        dpk_a_kpu_p_count.setText(c1Form.aDpkP.toString())
        dpt_c7_l_count.setText(c1Form.c7DptL.toString())
        dpt_c7_p_count.setText(c1Form.c7DptP.toString())
        dpt_b_c7_l_count.setText(c1Form.c7DptBL.toString())
        dpt_b_c7_p_count.setText(c1Form.c7DptBP.toString())
        dpk_c7_l_count.setText(c1Form.c7DpkL.toString())
        dpk_c7_p_count.setText(c1Form.c7DpkP.toString())
        disabilitas_l_count.setText(c1Form.disabilitasTerdaftarL.toString())
        disabilitas_p_count.setText(c1Form.disabilitasTerdaftarP.toString())
        disabilitas_l_voted_count.setText(c1Form.disabilitasHakPilihL.toString())
        disabilitas_p_voted_count.setText(c1Form.disabilitasHakPilihP.toString())
    }

    private fun EditText.getInt(): Int {
        return if (this.text.isNotEmpty()) {
            this.text.toString().toInt()
        } else {
            0
        }
    }

    private fun TextView.getInt(): Int {
        return if (this.text.isNotEmpty()) {
            this.text.toString().toInt()
        } else {
            0
        }
    }
}