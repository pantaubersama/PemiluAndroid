package com.pantaubersama.app.ui.merayakan.perhitungan.create.c1

import android.os.Bundle
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_c1_form.*
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

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var title: String = ""
        when (intent.getStringExtra(PantauConstants.Merayakan.C1_MODEL_TYPE)) {
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
        setupDptA3()
        setupDptA4()
        setupDpk()
    }

    private fun setupDpk() {
        RxTextView.textChanges(dpk_a_kpu_l_count)
            .flatMap {
                if (it.isEmpty()) {
                    Observable.just(0)
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
                val lpCount = if (dpk_a_kpu_p_count.text.isNotEmpty()) {
                    dpk_a_kpu_p_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpk_a_kpu_l_total_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpk_a_kpu_p_count)
            .flatMap {
                if (it.isEmpty()) {
                    Observable.just(0)
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
                val lpCount = if (dpk_a_kpu_l_count.text.isNotEmpty()) {
                    dpk_a_kpu_l_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpk_a_kpu_l_total_count.text = newCount.toString()
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
                    Observable.just(0)
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
                val lpCount = if (dpt_b_a4_kpu_p_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_p_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_a4_kpu_total_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_b_a4_kpu_p_count)
            .flatMap {
                if (it.isEmpty()) {
                    Observable.just(0)
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
                val lpCount = if (dpt_b_a4_kpu_l_count.text.isNotEmpty()) {
                    dpt_b_a4_kpu_l_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_b_a4_kpu_total_count.text = newCount.toString()
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
                    Observable.just(0)
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
                val lpCount = if (dpt_a3_kpu_p_count.text.isNotEmpty()) {
                    dpt_a3_kpu_p_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_a3_kpu_total_count.text = newCount.toString()
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()

        RxTextView.textChanges(dpt_a3_kpu_p_count)
            .flatMap {
                if (it.isEmpty()) {
                    Observable.just(0)
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
                val lpCount = if (dpt_a3_kpu_l_count.text.isNotEmpty()) {
                    dpt_a3_kpu_l_count.text.toString().toInt()
                } else {
                    0
                }
                val newCount = it + lpCount
                dpt_a3_kpu_total_count.text = newCount.toString()
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
}
